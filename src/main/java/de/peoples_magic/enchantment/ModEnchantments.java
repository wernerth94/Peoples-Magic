package de.peoples_magic.enchantment;

import de.peoples_magic.Util;
import de.peoples_magic.attributes.ModAttributes;
import de.peoples_magic.tag.ModTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> MAX_MANA = ResourceKey.create(Registries.ENCHANTMENT,
            Util.rec_loc("max_mana"));
    public static final ResourceKey<Enchantment> MANA_REGEN = ResourceKey.create(Registries.ENCHANTMENT,
            Util.rec_loc("mana_regen"));
    public static final ResourceKey<Enchantment> MANA_LEECH = ResourceKey.create(Registries.ENCHANTMENT,
            Util.rec_loc("mana_leech"));


    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var enchantment = context.lookup(Registries.ENCHANTMENT);
        var items = context.lookup(Registries.ITEM);

        register(context, MAX_MANA,
                Enchantment.enchantment(Enchantment.definition(
                                items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE), 7, 3,
                                Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(25, 8), 3, EquipmentSlotGroup.ARMOR))
                .exclusiveWith(enchantment.getOrThrow(ModTags.Enchantments.MANA_ENCHANTMENT))
                        .withEffect(EnchantmentEffectComponents.ATTRIBUTES,
                                new EnchantmentAttributeEffect(
                                        Util.rec_loc("enchantment.max_mana"),
                                        ModAttributes.MAX_MANA,
                                        LevelBasedValue.perLevel(10.0F),
                                        AttributeModifier.Operation.ADD_VALUE
                                )
                        )
        );

        register(context, MANA_REGEN,
                Enchantment.enchantment(Enchantment.definition(
                                items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE), 7, 3,
                                Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(25, 8), 3, EquipmentSlotGroup.ARMOR))
                        .exclusiveWith(enchantment.getOrThrow(ModTags.Enchantments.MANA_ENCHANTMENT))
                        .withEffect(EnchantmentEffectComponents.ATTRIBUTES,
                                new EnchantmentAttributeEffect(
                                        Util.rec_loc("enchantment.mana_regen"),
                                        ModAttributes.MANA_REGEN,
                                        LevelBasedValue.perLevel(0.15F),
                                        AttributeModifier.Operation.ADD_VALUE
                                )
                        )
        );

        register(context, MANA_LEECH,
                Enchantment.enchantment(Enchantment.definition(
                                items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                                7, 4,
                                Enchantment.dynamicCost(5, 8),
                                Enchantment.dynamicCost(25, 8),
                                3, EquipmentSlotGroup.MAINHAND))
                        .exclusiveWith(enchantment.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                        .withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM,
                                new ManaLeechEnchantmentEffect()
                        )
        );
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }
}
