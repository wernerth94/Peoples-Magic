package de.peoples_magic.entity.spells;

import de.peoples_magic.ai.FollowSummoningPlayerGoal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import java.util.function.Predicate;

public class SummonedZombie extends Zombie implements SummonedEntity {
    private Player owner = null;
    private int ticks_to_live = 0;


    public SummonedZombie(EntityType<? extends Zombie> entityType, Level level, int spell_level) {
        super(entityType, level);
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
        if (spell_level >= 3) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
        } else if (spell_level == 2) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        } else if (spell_level == 1) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
        }
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, true, new NotSummonedSelector()));

        this.goalSelector.addGoal(2, new ZombieAttackGoal(this, (double)1.0F, false));
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
