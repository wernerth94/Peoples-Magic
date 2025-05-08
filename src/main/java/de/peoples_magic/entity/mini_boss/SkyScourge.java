package de.peoples_magic.entity.mini_boss;

import de.peoples_magic.Config;
import de.peoples_magic.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


public class SkyScourge extends Phantom {
    private static final double FOLLOW_DISTANCE = 50F;
    private static final int GROUND_OFFSET = 20;
    private static final float CIRCLE_WIDTH = 40.0f;

    private final ServerBossEvent boss_event = new ServerBossEvent(Component.literal("Scourge of the Sky"),
            BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_10);

    private int phase;
    @Nullable
    protected LivingEntity target;
    protected TargetingConditions target_conditions;
    private AIState state;
    private boolean setup_done;
    private BlockPos anchor_point = BlockPos.ZERO;
    private Vec3 moveTargetPoint = Vec3.ZERO;
    private double circle_angle;
    private int ticks_since_last_charge;
    private int ticks_since_last_attack;
    private int charge_attack_counter;
    private int charge_attack_timer;

    private enum AIState {
        CIRCLE, CHARGING, RETREATING
    }

    public SkyScourge(EntityType<? extends SkyScourge> entityType, Level level) {
        super(entityType, level);

        this.setup_done = false;
        this.phase = 1;
        this.circle_angle = 0.0f;
        this.target_conditions = TargetingConditions.forCombat().range(FOLLOW_DISTANCE).selector(null);
        this.moveControl = new SkyScourgeMoveControl(this);

        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Config.sky_scourge_health);
        this.setHealth(this.getMaxHealth());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Config.sky_scourge_attack);
        reset_state();
    }

    protected Optional<ResourceKey<LootTable>> getDefaultLootTable() {
        return Optional.of(ResourceKey.create(Registries.LOOT_TABLE, Util.rec_loc("entities/sky_scourge")));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, (float)FOLLOW_DISTANCE + 15.0f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 200)
                .add(Attributes.MOVEMENT_SPEED, .95D)
                .add(Attributes.ATTACK_DAMAGE, 12)
                .add(Attributes.ATTACK_KNOCKBACK, 4f)
                .add(Attributes.FOLLOW_RANGE, FOLLOW_DISTANCE)
                .add(Attributes.STEP_HEIGHT, 1.0)
                ;
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);

        if (setup_done == false) {
            setup_done = true;
            if (this.anchor_point.getX() == 0 && this.anchor_point.getZ() == 0 && this.anchor_point.getY() == 0) {
                setup_circle_height();
            }
            next_circle_target();
            this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 999999999, 2, false, false, false));
        }

        if (this.isOnFire()) {
            this.extinguishFire();
        }

        Player next_target = findTarget();
        if (target != null && next_target == null) {
            reset_state();
        }
        this.target = next_target;

        if (this.phase == 1 && this.getHealth() <= 70) {
            this.phase = 2;
        }

        if (this.state == AIState.CIRCLE) {
            do_circle();
            if (this.target != null) {
                ticks_since_last_charge++;
                if (ticks_since_last_charge > 20 * 8) {
                    ticks_since_last_charge = 0;
                    this.state = AIState.CHARGING;
                }
            }
        }
        else if (this.state == AIState.CHARGING) {
            do_charge();
        }
    }


    private void do_charge() {
        this.moveTargetPoint = this.target.position();
        this.setTarget(this.target);
        ticks_since_last_attack++;
        charge_attack_timer++;
        if (ticks_since_last_attack > 20 * 1 && reached_target()) {
            doHurtTarget((ServerLevel) this.level(), this.target);
            check_and_reduce_shield(this.target);
            if (this.phase == 2) {
                target.igniteForSeconds(2f);
            }
            ticks_since_last_attack = 0;
            charge_attack_counter++;
            if (charge_attack_counter >= 3 || charge_attack_timer > 20 * 7) {
                this.state = AIState.CIRCLE;
                this.setTarget(null);
                this.next_circle_target();
                charge_attack_counter = 0;
                charge_attack_timer = 0;
            }
        }

        if (net.neoforged.neoforge.event.EventHooks.canEntityGrief((ServerLevel) this.level(), this)) { // copied from Ravager
            AABB aabb = this.getBoundingBox().inflate(0.1);
            for (BlockPos blockpos : BlockPos.betweenClosed(
                    Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ)
            )) {
                this.level().destroyBlock(blockpos, true, this);
            }
        }
    }


    private void do_circle() {
        if (reached_target()) {
            next_circle_target();
        }
    }


    private void next_circle_target() {
        this.circle_angle += 0.1F;
        double radian = this.circle_angle * 2 * Math.PI;
        double direction_x = CIRCLE_WIDTH * Math.cos(radian);
        double direction_z = CIRCLE_WIDTH * Math.sin(radian);
        Vec3 pos = new Vec3(this.anchor_point.getX() + direction_x, this.anchor_point.getY(), this.anchor_point.getZ() + direction_z);
        this.moveTargetPoint = pos;
    }


    private void setup_circle_height() {
        int height = (int)this.getY();
        double orig_x = this.getX();
        double orig_z = this.getZ();
        int j;
        for (j = height; j < 300; j++) {
            boolean clear = true;
            for (float i = 0.0f; i < 1.0; i += 0.01f) {
                double direction_radian = i * 2 * Math.PI;
                double direction_x = 1.5f * CIRCLE_WIDTH * Math.cos(direction_radian);
                double direction_z = 1.5f * CIRCLE_WIDTH * Math.sin(direction_radian);
                BlockPos pos = new BlockPos((int) (orig_x + direction_x), j, (int) (orig_z + direction_z));
                if (!(this.level().getBlockState(pos).getBlock() instanceof AirBlock)) {
                    clear = false;
                    break;
                }
            }
            if (clear) {
                break;
            }
        }
        j += GROUND_OFFSET;
        this.anchor_point = new BlockPos((int) orig_x, j, (int) orig_z);
        this.setPos(this.anchor_point.getX(), j, this.anchor_point.getZ());
    }


    private void check_and_reduce_shield(LivingEntity entity) {
        final int damage_amount = 30;
        ItemStack main = entity.getMainHandItem();
        ItemStack off = entity.getOffhandItem();
        if (entity.isBlocking()) {
            if (entity.isUsingItem()) {
                if (off.is(Items.SHIELD)) {
                    off.hurtAndBreak(damage_amount, entity, EquipmentSlot.OFFHAND);
                }
                else if (main.is(Items.SHIELD)) {
                    main.hurtAndBreak(damage_amount, entity, EquipmentSlot.MAINHAND);
                }
            }
        }
    }

    @Override
    public boolean hurtServer(ServerLevel server_level, DamageSource damage_source, float amount) {
        if (damage_source.is(DamageTypeTags.IS_PROJECTILE) || !damage_source.isDirect()) {
            amount += 0.5f;
        }
        return super.hurtServer(server_level, damage_source, amount);
    }


    @Override
    public void tick() {
        super.tick();
        if (this.phase == 2) {
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 1, this.getZ(), 0.0, 0.0, 0.0);
            this.level().addParticle(ParticleTypes.LAVA, this.getX(), this.getY() + 1, this.getZ(), 0.0, 0.0, 0.0);
        }
    }


    private boolean reached_target() {
        if (this.state == AIState.CHARGING) {
            return this.position().distanceTo(this.moveTargetPoint) < 9.0;
        }
        else if (this.state == AIState.CIRCLE) {
            return this.position().distanceTo(this.moveTargetPoint) < 2.0;
        }
        else {
            return this.position().distanceTo(this.moveTargetPoint) < 1.0;
        }
    }


    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }


    @Override
    protected boolean isSunBurnTick() {
        return false;
    }


    private void reset_state() {
        if (this.state != AIState.CIRCLE) {
            this.state = AIState.CIRCLE;
            this.ticks_since_last_charge = 999999;
            this.ticks_since_last_attack = 999999;
            this.charge_attack_counter = 0;
            this.charge_attack_timer = 0;
        }
    }


    protected Player findTarget() {
        ServerLevel serverlevel = (ServerLevel) this.level();
        return serverlevel.getNearestPlayer(this.target_conditions.range(FOLLOW_DISTANCE), this,
                this.getX(), this.getEyeY(), this.getZ());
    }


    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.boss_event.addPlayer(serverPlayer);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.boss_event.removePlayer(serverPlayer);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        float percent_health = this.getHealth() / this.getMaxHealth();
        this.boss_event.setProgress(percent_health);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("AX")) {
            this.anchor_point = new BlockPos(compound.getInt("AX").orElse(0),
                    compound.getInt("AY").orElse(0),
                    compound.getInt("AZ").orElse(0));
            next_circle_target();
        }
        if (compound.contains("phase")) {
            this.phase = compound.getInt("phase").orElse(0);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("AX", this.anchor_point.getX());
        compound.putInt("AY", this.anchor_point.getY());
        compound.putInt("AZ", this.anchor_point.getZ());
        compound.putInt("phase", this.phase);
    }


    public static boolean checkSpawnRules(
            EntityType<? extends Mob> type, LevelAccessor level, EntitySpawnReason spawnType, BlockPos pos, RandomSource random
    ) {
        BlockPos blockpos = pos.below();
        return spawnType == EntitySpawnReason.SPAWNER || level.getBlockState(blockpos).isValidSpawn(level, blockpos, EntityType.ALLAY);
    }

    class SkyScourgeMoveControl extends MoveControl {
        private float speed = 0.1F;

        public SkyScourgeMoveControl(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (SkyScourge.this.horizontalCollision) {
                SkyScourge.this.setYRot(SkyScourge.this.getYRot() + 180.0F);
                this.speed = 0.1F;
            }

            double d0 = SkyScourge.this.moveTargetPoint.x - SkyScourge.this.getX();
            double d1 = SkyScourge.this.moveTargetPoint.y - SkyScourge.this.getY();
            double d2 = SkyScourge.this.moveTargetPoint.z - SkyScourge.this.getZ();
            double xz_dist_to_target = Math.sqrt(d0 * d0 + d2 * d2);
            if (Math.abs(xz_dist_to_target) > 1.0E-5F) {
                double d4 = 1.0 - Math.abs(d1 * 0.7F) / xz_dist_to_target;
                d0 *= d4;
                d2 *= d4;
                xz_dist_to_target = Math.sqrt(d0 * d0 + d2 * d2);
                double dist_to_target = Math.sqrt(d0 * d0 + d2 * d2 + d1 * d1);
                float f = SkyScourge.this.getYRot();
                float f1 = (float) Mth.atan2(d2, d0);
                float f2 = Mth.wrapDegrees(SkyScourge.this.getYRot() + 90.0F);
                float f3 = Mth.wrapDegrees(f1 * (180.0F / (float)Math.PI));
                SkyScourge.this.setYRot(Mth.approachDegrees(f2, f3, 4.0F) - 90.0F);
                SkyScourge.this.yBodyRot = SkyScourge.this.getYRot();
                if (Mth.degreesDifferenceAbs(f, SkyScourge.this.getYRot()) < 3.0F) {
                    this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
                } else {
                    this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
                }

                float f4 = (float)(-(Mth.atan2(-d1, xz_dist_to_target) * 180.0F / (float)Math.PI));
                SkyScourge.this.setXRot(f4);
                float f5 = SkyScourge.this.getYRot() + 90.0F;
                double d6 = (double)(this.speed * Mth.cos(f5 * (float) (Math.PI / 180.0))) * Math.abs(d0 / dist_to_target);
                double d7 = (double)(this.speed * Mth.sin(f5 * (float) (Math.PI / 180.0))) * Math.abs(d2 / dist_to_target);
                double d8 = (double)(this.speed * Mth.sin(f4 * (float) (Math.PI / 180.0))) * Math.abs(d1 / dist_to_target);
                Vec3 current_delta = SkyScourge.this.getDeltaMovement();
                SkyScourge.this.setDeltaMovement(current_delta.add(new Vec3(d6, d8, d7).subtract(current_delta).scale(0.2)));
            }
        }
    }
}
