package de.peoples_magic.entity.spells;

import de.peoples_magic.ai.FollowSummoningPlayerGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SummonedHorse extends Horse implements SummonedEntity {
    private Player owner = null;
    private int ticks_to_live = 0;


    public SummonedHorse(EntityType<? extends Horse> entityType, Level level, int spell_level) {
        super(entityType, level);
        this.setTamed(true);
//        this.equipSaddle(new ItemStack(Items.SADDLE), null);
        this.equipBodyArmor(null, new ItemStack(Items.DIAMOND_HORSE_ARMOR));
        AttributeInstance speedAttribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttribute != null) {
            speedAttribute.setBaseValue(0.45D); // Set to your desired speed value
        }
        AttributeInstance jumpAttribute = this.getAttribute(Attributes.JUMP_STRENGTH);
        if (jumpAttribute != null) {
            jumpAttribute.setBaseValue(0.8D); // Set to your desired speed value
        }
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
