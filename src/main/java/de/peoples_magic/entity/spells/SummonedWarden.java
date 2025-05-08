package de.peoples_magic.entity.spells;

import de.peoples_magic.ai.FollowSummoningPlayerGoal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SummonedWarden extends Warden implements SummonedEntity {
    private Player owner = null;
    private int ticks_to_live = 0;
    private NearestAttackableTargetGoal target_goal;
    private MeleeAttackGoal melee_goal;
    private LivingEntity previous_target = null;
    private LivingEntity target = null;

    public SummonedWarden(EntityType<? extends Warden> entityType, Level level, int spell_level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.target_goal = new NearestAttackableTargetGoal<>(this, Monster.class, true, new NotSummonedSelector());
        this.targetSelector.addGoal(2, this.target_goal);

        this.melee_goal = new MeleeAttackGoal(this, 1.0F, false);
        this.goalSelector.addGoal(2, melee_goal);
        this.goalSelector.addGoal(5, new FollowSummoningPlayerGoal(this, 1.3f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public @Nullable LivingEntity getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            ticks_to_live--;
            if (ticks_to_live <= 0) {
                this.discard();
            }
        }
    }

    @Override
    protected void customServerAiStep(ServerLevel p_376677_) {
        // stop usual warden behavior
        //super.customServerAiStep(p_376677_);
    }

    @Override
    public boolean hurtServer(ServerLevel server_level, DamageSource damage_source, float amount) {
        boolean flag = super.hurtServer(server_level, damage_source, amount);
        if (damage_source.getEntity() instanceof Player) {
            this.target_goal.stop();
            this.target_goal.canUse();
        }
        return flag;
    }

    @Override
    public Player get_owner() {
        return owner;
    }

    @Override
    public void set_owner(Player p) {
        owner = p;
    }

    @Override
    public int get_ticks_to_live() {
        return ticks_to_live;
    }

    @Override
    public void set_ticks_to_live(int t) {
        ticks_to_live = t;
    }
}
