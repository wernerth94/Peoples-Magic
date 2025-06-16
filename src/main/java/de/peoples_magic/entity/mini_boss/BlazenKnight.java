package de.peoples_magic.entity.mini_boss;

import com.google.common.collect.Lists;
import de.peoples_magic.Config;
import de.peoples_magic.Util;
import de.peoples_magic.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Optional;

public class BlazenKnight extends WitherSkeleton {
    private static final double FOLLOW_DISTANCE = 20F;
    private static final int CANT_REACH_PATIENCE = 20 * 5;

    private final ServerBossEvent boss_event = new ServerBossEvent(Component.literal("The Blazen Knight"),
            BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);

    public SkeletonHorse horse;
    private int phase;
    @Nullable
    protected LivingEntity target;
    protected TargetingConditions target_conditions;
    private AIState state;
    private int ticks_since_last_charge;
    private Vec3 retreat_position;
    private int cant_reach_count;
    private int ticks_since_last_shot;
    private boolean setup_done;
    private int ticks_alive;

    private enum AIState {
        IDLE, CHARGING, RETREATING, SHOOTING
    }

    public BlazenKnight(EntityType<? extends BlazenKnight> entityType, Level level) {
        super(entityType, level);

        this.setup_done = false;
        this.phase = 1;
        this.target_conditions = TargetingConditions.forCombat().range(FOLLOW_DISTANCE).selector(null);
        this.state = AIState.IDLE;
        this.cant_reach_count = 0;
        this.ticks_since_last_shot = 0;
        this.ticks_alive = 0;

        ArrayList<DyeItem> dye_list = Lists.newArrayList();
        dye_list.add((DyeItem) Items.RED_DYE);

        this.setItemSlot(EquipmentSlot.HEAD, DyedItemColor.applyDyes(new ItemStack(Items.LEATHER_HELMET), dye_list));
        this.setItemSlot(EquipmentSlot.CHEST, DyedItemColor.applyDyes(new ItemStack(Items.LEATHER_CHESTPLATE), dye_list));
        this.setItemSlot(EquipmentSlot.LEGS, DyedItemColor.applyDyes(new ItemStack(Items.LEATHER_LEGGINGS), dye_list));
        this.setItemSlot(EquipmentSlot.FEET, DyedItemColor.applyDyes(new ItemStack(Items.LEATHER_BOOTS), dye_list));

        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));

        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Config.blazen_knight_health);
        this.setHealth(this.getMaxHealth());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Config.blazen_knight_attack);
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, (float)FOLLOW_DISTANCE + 5.0f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.MOVEMENT_SPEED, .3D)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5f)
                .add(Attributes.FOLLOW_RANGE, 20)
                ;
    }

    private void setup(ServerLevel level) {
        // fix spawning weirdness, where the Knight can spawn underground
        if (!level.canSeeSky(this.blockPosition())) {
            BlockPos above = this.blockPosition().above();
            while (!level.canSeeSky(above) && above.getY() < 150) {
                above = above.above();
            }
            if (level.canSeeSky(above)) {
                this.setPos(above.getX() + 0.5, above.getY() + 1, above.getZ() + 0.5);
            }
            else {
                System.out.println("Failed to find a valid spawn position for Blazen Knight");
            }
        }
        SkeletonHorse skelly_horse = new SkeletonHorse(EntityType.SKELETON_HORSE, level);
        skelly_horse.setTamed(true);
        skelly_horse.equipItemIfPossible(level, new ItemStack(Items.SADDLE));
        skelly_horse.setPos(this.position());
        AttributeInstance speedAttribute = skelly_horse.getAttribute(Attributes.MOVEMENT_SPEED);
        speedAttribute.setBaseValue(0.4F);
        level.addFreshEntity(skelly_horse);
        this.startRiding(skelly_horse);
        this.horse = skelly_horse;
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);
        if (!setup_done) {
            setup(level);
            this.setup_done = true;
        }

        ticks_alive++;
        if (ticks_alive > 20 * 60 * 5) {
            // kill after 5 minutes and when no target is active
            if (this.target == null) {
                this.remove(RemovalReason.DISCARDED);
            }
        }

        if (!canUse()) {
            return;
        }
        ticks_alive = 0;

        float percent_health = this.getHealth() / this.getMaxHealth();
        if (this.phase == 1 && percent_health <= 0.5) {
            begin_phase_2();
            this.phase = 2;
        }

        if (!canReach(this.target)) {
            this.cant_reach_count++;
            if (this.cant_reach_count > CANT_REACH_PATIENCE) {
                this.state = AIState.SHOOTING;
            }
        }
        else {
            this.cant_reach_count = 0;
            if (this.state == AIState.SHOOTING) {
                this.ticks_since_last_charge = 0;
                this.retreat_position = null;
                this.state = AIState.CHARGING;
            }
        }

        switch (this.state) {
            case IDLE:
            case RETREATING:
                this.ticks_since_last_charge++;
                double dist = this.target != null ? this.distanceTo(this.target) : 100000F;
                if (this.ticks_since_last_charge > 20 * 4 ||
                    (this.ticks_since_last_charge > 20 * 2 && dist < 4)) {

                    this.ticks_since_last_charge = 0;
                    this.retreat_position = null;
                    this.state = AIState.CHARGING;
                }
                else {
                    if (this.retreat_position == null) {
                        this.retreat_position = this.find_retreat_position();
                        if (horse_alive()) {
                            this.horse.getNavigation().stop();
                            this.horse.getNavigation().moveTo(this.retreat_position.x, this.retreat_position.y, this.retreat_position.z,
                                    1.0);
                        }
                        else {
                            this.navigation.stop();
                            this.navigation.moveTo(this.retreat_position.x, this.retreat_position.y, this.retreat_position.z,
                                    1.0);
                        }
                    }
                }
                break;

            case CHARGING:
                double d3 = this.distanceTo(this.target);
                if (!(d3 <= 2.5) || !horse_alive()) {
                    this.setTarget(this.target);
                } else {
                    this.setTarget(null);
                    this.state = AIState.RETREATING;
                }
                break;

            case SHOOTING:
                this.ticks_since_last_shot++;
                if (this.ticks_since_last_shot > 20 * 2 && this.getSensing().hasLineOfSight(this.target)) {
                    this.ticks_since_last_shot = 0;
                    double d0 = this.distanceToSqr(this.target);
                    double d1 = this.target.getX() - this.getX();
                    double d2 = this.target.getY(0.5) - this.getY(0.5);
                    d3 = this.target.getZ() - this.getZ();
                    double d4 = Math.sqrt(Math.sqrt(d0)) * 0.5;
                    Vec3 vec3 = new Vec3(this.getRandom().triangle(d1, 1.0 * d4), d2, this.getRandom().triangle(d3, 1.0 * d4));
                    SmallFireball smallfireball = new SmallFireball(this.level(), this, vec3.normalize());
                    smallfireball.setPos(smallfireball.getX(), this.getY(0.5) + 0.5, smallfireball.getZ());
                    this.level().addFreshEntity(smallfireball);
                }
                break;
        }
    }

    private void begin_phase_2() {
        if (!this.level().isClientSide) {
            for (int i = 0; i < 2; i++) {
                Vex vex = new Vex(EntityType.VEX, this.level());
                vex.setPos(this.position());
                this.level().addFreshEntity(vex);
                vex.setTarget(this.target);
                vex.setLastHurtByMob(this.target);
                vex.setLastHurtByMob(this.target);
//                vex.setLastHurtByPlayer((Player)this.target);
            }
        }
    }


    @Override
    public void remove(RemovalReason reason) {
        if (!reason.equals(RemovalReason.KILLED)) {
            if (this.horse_alive() && this.isPassenger()) {
                this.horse.discard();
            }
        }
        super.remove(reason);
    }

    private boolean canReach(LivingEntity target) {
        Path path = this.navigation.createPath(target, 0);
        if (path == null) {
            return false;
        } else {
            Node node = path.getEndNode();
            if (node == null) {
                return false;
            } else {
                int i = node.x - target.getBlockX();
                int j = node.z - target.getBlockZ();
                int k = node.y - target.getBlockY();
                return (double)(i * i + j * j + k * k) <= 9;
            }
        }
    }

    @Override
    protected boolean isSunBurnTick() {
        return false;
    }

    private boolean horse_alive() {
        return this.horse != null && !this.horse.isRemoved();
    }

    private Vec3 find_retreat_position() {
        double orig_x = this.getX();
        double orig_z = this.getZ();
        double random_direction_radian = Math.random() * 2 * Math.PI;
        double distance = 15;  // 10 blocks
        double direction_x = distance * Math.cos(random_direction_radian);
        double direction_z = distance * Math.sin(random_direction_radian);
        return new Vec3(orig_x + direction_x, this.getY(), orig_z + direction_z);
    }

    private void reset_state() {
        if (this.state != AIState.IDLE) {
            this.state = AIState.IDLE;
            this.ticks_since_last_charge = 100000;
            this.retreat_position = null;
            this.cant_reach_count = 0;
            this.ticks_since_last_shot = 0;
        }
    }

    public boolean canUse() {
        this.findTarget();
        boolean can_use = this.target != null;
        if (!can_use) {
            this.reset_state();
        }
        return can_use;
    }

    protected void findTarget() {
        ServerLevel serverlevel = (ServerLevel) this.level();
        this.target = serverlevel.getNearestPlayer(this.target_conditions.range(FOLLOW_DISTANCE), this,
                this.getX(), this.getEyeY(), this.getZ());
    }


    public static boolean checkSpawnRules(
            EntityType<? extends Monster> type, LevelAccessor level, EntitySpawnReason spawnType, BlockPos pos, RandomSource random
    ) {
        BlockPos blockpos = pos.below();
//        for (int i = 1; i <= 3; ++i) {
//            if (!(level.getBlockState(new BlockPos(pos.getX(), blockpos.getY() + i, pos.getZ())).getBlock() instanceof AirBlock)) {
//                return false;
//            }
//        }

        return spawnType == EntitySpawnReason.SPAWNER ||
//                (level.canSeeSky(pos) &&
                 level.getBlockState(blockpos).isValidSpawn(level, blockpos, EntityType.ENDERMAN);
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
        if (compound.contains("phase")) {
            this.phase = compound.getIntOr("phase", 0);
        }
        if(compound.contains("ticks_alive")) {
            this.ticks_alive = compound.getIntOr("ticks_alive", 0);
        }
        if(compound.contains("setup_done")) {
            this.setup_done = compound.getBooleanOr("setup_done", false);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("phase", this.phase);
        compound.putInt("ticks_alive", ticks_alive);
        compound.putBoolean("setup_done", setup_done);
    }

}
