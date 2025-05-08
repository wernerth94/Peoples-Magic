package de.peoples_magic.enchantment;

import com.mojang.serialization.MapCodec;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public class ManaLeechEnchantmentEffect implements EnchantmentEntityEffect {
    public static final MapCodec<ManaLeechEnchantmentEffect> CODEC = MapCodec.unit(ManaLeechEnchantmentEffect::new);

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        if (entity instanceof LivingEntity) {
            Player owner = (Player) item.owner();
            if (owner.getAttackStrengthScale(0.5f) >= 1.0) {
                int leeched_amount = 4 * enchantmentLevel;
                double player_mana = owner.getData(ModAttachments.PLAYER_MANA);
                Util.update_player_mana(owner, player_mana + leeched_amount);
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
