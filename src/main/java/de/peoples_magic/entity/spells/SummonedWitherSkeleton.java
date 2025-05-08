package de.peoples_magic.entity.spells;

import de.peoples_magic.ai.FollowSummoningPlayerGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class SummonedWitherSkeleton extends WitherSkeleton implements SummonedEntity {
    private Player owner = null;
    private int ticks_to_live = 0;


    public SummonedWitherSkeleton(EntityType<? extends WitherSkeleton> entityType, Level level) {
        super(entityType, level);
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
    }

    @Override
    protected void registerGoals() {
        TargetingConditions.Selector isNotSummoned = (entity, serverLevel) -> entity instanceof Monster && !(entity instanceof SummonedEntity);
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, true, isNotSummoned));

        this.goalSelector.addGoal(5, new FollowSummoningPlayerGoal(this, 1.3f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }


    @Override
    public void tick() {
        super.tick();
        if (ticks_to_live > 0) {
            ticks_to_live--;
        }
        else {
            if (!this.level().isClientSide) {
                this.discard();
            }
        }
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
