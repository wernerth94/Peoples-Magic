package de.peoples_magic.entity.spells;

import de.peoples_magic.ai.FollowSummoningPlayerGoal;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import java.util.function.Predicate;

public class SummonedSkeleton extends Skeleton implements SummonedEntity {
    private Player owner = null;
    private int ticks_to_live = 0;


    public SummonedSkeleton(EntityType<? extends Skeleton> entityType, Level level, int spell_level) {
        super(entityType, level);
        Registry<Enchantment> reg = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
        if (spell_level >= 5) {
            ItemStack bow = new ItemStack(Items.BOW);
            bow.enchant(reg.getOrThrow(Enchantments.POWER), 4);
            this.setItemSlot(EquipmentSlot.MAINHAND, bow);
        } else if (spell_level == 4) {
            ItemStack bow = new ItemStack(Items.BOW);
            bow.enchant(reg.getOrThrow(Enchantments.POWER), 2);
            this.setItemSlot(EquipmentSlot.MAINHAND, bow);
        } else if (spell_level == 3) {
            ItemStack bow = new ItemStack(Items.BOW);
            this.setItemSlot(EquipmentSlot.MAINHAND, bow);
        }
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, true, new NotSummonedSelector()));

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
