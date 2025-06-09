package de.peoples_magic.entity.mini_boss;

import de.peoples_magic.Config;
import de.peoples_magic.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.random.RandomGenerator;


public class ForestGuardian extends Creaking {
    private static final double FOLLOW_DISTANCE = 50F;
    private static final float SEARCH_WIDTH = 20.0f;

    private final ServerBossEvent boss_event = new ServerBossEvent(Component.literal("Guardian of the Forest"),
            BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.NOTCHED_10);

    private int phase;
    @Nullable
    protected LivingEntity target;
    protected TargetingConditions target_conditions;
    private AIState state;
    private boolean setup_done;
    private BlockPos anchor_point = BlockPos.ZERO;
    private Vec3 target_position;
    private Vec3 tree_to_hide;
    private int find_tree_timer;
    private Vec3 approaching_direction = Vec3.ZERO;
    private int approach_timer;
    private int ticks_since_last_attack;
    private boolean first_tree_search;
    private int stuck_detection_counter;
    private Vec3 last_position;
    private int ticks_alive;

    private enum AIState {
        IDLE, APPROACHING, RETREATING
    }

    public ForestGuardian(EntityType<? extends ForestGuardian> entityType, Level level) {
        super(entityType, level);

        this.setup_done = false;
        this.phase = 1;
        this.target_conditions = TargetingConditions.forCombat().ignoreLineOfSight().range(FOLLOW_DISTANCE).selector(null);
        this.ticks_alive = 0;

        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Config.forest_guardian_health);
        this.setHealth(this.getMaxHealth());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Config.forest_guardian_attack);
        reset_state();
    }

    protected Optional<ResourceKey<LootTable>> getDefaultLootTable() {
        return Optional.of(ResourceKey.create(Registries.LOOT_TABLE, Util.rec_loc("entities/forest_guardian")));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, (float)FOLLOW_DISTANCE + 15.0f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 120)
                .add(Attributes.MOVEMENT_SPEED, .4D)
                .add(Attributes.ATTACK_DAMAGE, 15)
                .add(Attributes.ATTACK_KNOCKBACK, 1f)
                .add(Attributes.FOLLOW_RANGE, FOLLOW_DISTANCE)
                .add(Attributes.STEP_HEIGHT, 1.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 4.0)
                ;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float amount) {
        if (damageSource.isDirect() && ticks_since_last_attack > 40 &&
            damageSource.getEntity() instanceof Player player) {
            ticks_since_last_attack = 0;
//            doHurtTarget(serverLevel, player);
            this.target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 60, 2, false, false));
            find_tree_timer = 9999;
        }

        return super.hurtServer(serverLevel, damageSource, amount);
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
//        super.customServerAiStep(level);

        if (!setup_done) {
            setup_done = true;

        }

        ticks_alive++;
        if (ticks_alive > 20 * 60 * 5) {
            // kill after 5 minutes and when no target is active
            if (this.target == null) {
                this.remove(RemovalReason.DISCARDED);
            }
        }

        Player next_target = findTarget();
        if (target != null && next_target == null) {
            reset_state();
            return;
        }
        this.target = next_target;


        if (this.target != null) {
            ticks_alive = 0;
            // Stuck detection
            if (this.position().distanceTo(this.last_position) < 1.5) {
                stuck_detection_counter++;
            }
            this.last_position = new Vec3(this.position().x, this.position().y, this.position().z);
            if (stuck_detection_counter > 40) {
                stuck_detection_counter = 0;
                this.state = AIState.RETREATING;
                this.first_tree_search = true;
                this.find_tree_timer = 9999;
            }
            // Attack overwrite
            ticks_since_last_attack++;
            if (ticks_since_last_attack > 15 * 20) {
                ticks_since_last_attack = 0;
                this.state = AIState.APPROACHING;
            }

            if (this.state == AIState.IDLE) {
                this.state = AIState.RETREATING;
                do_retreat();
            }
            else if (this.state == AIState.RETREATING) {
                do_retreat();
            }
            else if (this.state == AIState.APPROACHING) {
                do_approach();
            }
        }
    }


    private void do_approach() {
        while (approaching_direction.length() == 0) {
            int x = RandomGenerator.getDefault().nextInt(-1, 2);
            int z = RandomGenerator.getDefault().nextInt(-1, 2);
            approaching_direction = new Vec3(x, 0, z);
        }

        approach_timer++;
        if (approach_timer > 20) {
            approach_timer = 0;
            this.target_position = this.target.position().add(approaching_direction);
            this.navigation.stop();
            this.navigation.moveTo(this.target_position.x, this.target_position.y, this.target_position.z, 0.8);
        }

        if (this.position().distanceTo(this.target_position) < 1.5) {
            this.state = AIState.RETREATING;
            this.first_tree_search = true;
            this.removeEffect(MobEffects.INVISIBILITY);
            this.approaching_direction = Vec3.ZERO;
            this.tree_to_hide = null;
            this.ticks_since_last_attack = 0;
            doHurtTarget((ServerLevel) this.level(), this.target);
            this.target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 60, 2, false, false));
        }
    }


    private void do_retreat() {
        find_tree_timer++;
        if (find_tree_timer > 20) {
            find_tree_timer = 0;
            find_tree_to_hide();
            first_tree_search = false;
        }
        else if (this.tree_to_hide != null){
            if (this.position().distanceTo(this.tree_to_hide) < 1.0) {
                this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 200, 0, false, false));
                this.state = AIState.APPROACHING;
            }
        }
    }


    private void find_tree_to_hide() {
        ServerLevel server_level = (ServerLevel) this.level();
        AABB search_area = new AABB(this.blockPosition());
        search_area = search_area.inflate(SEARCH_WIDTH, 5, SEARCH_WIDTH);
        List<Tuple<BlockPos, Double>> candidates = new ArrayList<>();
        for (BlockPos blockpos : BlockPos.betweenClosed(
                Mth.floor(search_area.minX), Mth.floor(search_area.minY), Mth.floor(search_area.minZ),
                Mth.floor(search_area.maxX), Mth.floor(search_area.maxY), Mth.floor(search_area.maxZ)
        )) {
            Block block = server_level.getBlockState(blockpos).getBlock();
            if (!block.equals(Blocks.AIR) && !block.equals(Blocks.DIRT) && !block.equals(Blocks.STONE)) {
                if (!first_tree_search || this.position().distanceTo(new Vec3(blockpos)) > 3) {
                    if (is_wood(server_level.getBlockState(blockpos)) &&
                            is_wood(server_level.getBlockState(blockpos.above()))) {
                        candidates.add(new Tuple<>(blockpos.immutable(), this.distanceToSqr(blockpos.getX(), blockpos.getY(), blockpos.getZ())));
                    }
                }
            }
        }

        candidates.sort(Comparator.comparing(Tuple::getB));
        for (Tuple<BlockPos, Double> candidate : candidates) {
            Vec3 candidate_pos = new Vec3(candidate.getA());
            Vec3 direction = candidate_pos.subtract(this.target.position()).multiply(1, 0, 1).normalize();
            Vec3 hiding_area = candidate_pos.add(direction);
            BlockPos hiding_block = new BlockPos((int)Math.round(hiding_area.x), (int)Math.round(hiding_area.y), (int)Math.round(hiding_area.z));
            if (is_open(server_level.getBlockState(hiding_block)) &&
                is_open(server_level.getBlockState(hiding_block.above()))) {

                Path path = this.navigation.createPath(hiding_block, 0);
                if (path != null) {
                    this.tree_to_hide = hiding_area;
                    this.navigation.stop();
                    Vec3 exact_spot = new Vec3(candidate.getA()).add(direction.multiply(1.2, 1, 1.2));
                    this.tree_to_hide = exact_spot;
                    this.navigation.moveTo(exact_spot.x, exact_spot.y, exact_spot.z, 1);
                    return;
                }
            }
        }
    }


    private boolean is_wood(BlockState block_state) {
        return block_state.is(BlockTags.LOGS);
    }

    private boolean is_open(BlockState block_state) {
        return block_state.is(Blocks.AIR) ||
                block_state.is(BlockTags.FLOWERS) || block_state.is(BlockTags.SMALL_FLOWERS) ||
                block_state.is(Blocks.FERN) || block_state.is(Blocks.TALL_GRASS) || block_state.is(Blocks.SHORT_GRASS);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.phase == 2) {
//            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 1, this.getZ(), 0.0, 0.0, 0.0);
//            this.level().addParticle(ParticleTypes.LAVA, this.getX(), this.getY() + 1, this.getZ(), 0.0, 0.0, 0.0);
        }
    }


    @Override
    protected boolean isSunBurnTick() {
        return false;
    }


    private void reset_state() {
        if (this.state != AIState.IDLE) {
            this.state = AIState.IDLE;
            this.approach_timer = 999999;
            this.ticks_since_last_attack = 999999;
            this.find_tree_timer = 999999;
            this.tree_to_hide = null;
            this.approaching_direction = Vec3.ZERO;
            this.first_tree_search = true;
            this.stuck_detection_counter = 0;
            this.last_position = this.position();
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
        }
        if (compound.contains("phase")) {
            this.phase = compound.getInt("phase").orElse(0);
        }
        if(compound.contains("ticks_alive")) {
            this.ticks_alive = compound.getIntOr("ticks_alive", 0);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("AX", this.anchor_point.getX());
        compound.putInt("AY", this.anchor_point.getY());
        compound.putInt("AZ", this.anchor_point.getZ());
        compound.putInt("phase", this.phase);
        compound.putInt("ticks_alive", ticks_alive);
    }


    public static boolean checkSpawnRules(
            EntityType<? extends Mob> type, LevelAccessor level, EntitySpawnReason spawnType, BlockPos pos, RandomSource random
    ) {
        BlockPos blockpos = pos.below();
        return spawnType == EntitySpawnReason.SPAWNER || level.getBlockState(blockpos).isValidSpawn(level, blockpos, EntityType.ENDERMAN);
    }
}
