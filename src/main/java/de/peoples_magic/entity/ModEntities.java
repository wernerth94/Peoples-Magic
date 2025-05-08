package de.peoples_magic.entity;

import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.Util;
import de.peoples_magic.entity.mini_boss.BlazenKnight;
import de.peoples_magic.entity.mini_boss.ForestGuardian;
import de.peoples_magic.entity.mini_boss.SkyScourge;
import de.peoples_magic.entity.spells.AetherGripProjectile;
import de.peoples_magic.entity.spells.CustomLightning;
import de.peoples_magic.entity.spells.FireballProjectile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, PeoplesMagicMod.MOD_ID);

    public static final Supplier<EntityType<FireballProjectile>> FIREBALL_PROJECTILE = ENTITY_TYPES.register("fireball",
            () -> EntityType.Builder.<FireballProjectile>of(FireballProjectile::new, MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(4).updateInterval(10)
                    .build(Util.rec_key(Registries.ENTITY_TYPE, "fireball")));

    public static final Supplier<EntityType<AetherGripProjectile>> AETHER_GRIP_PROJECTILE = ENTITY_TYPES.register("aether_grip",
            () -> EntityType.Builder.<AetherGripProjectile>of(AetherGripProjectile::new, MobCategory.MISC).sized(1F, 1F)
                    .build(Util.rec_key(Registries.ENTITY_TYPE, "aether_grip")));

    public static final Supplier<EntityType<BlazenKnight>> BLAZEN_KNIGHT = ENTITY_TYPES.register("blazen_knight",
            () -> EntityType.Builder.<BlazenKnight>of(BlazenKnight::new, MobCategory.MONSTER)
                    .sized(1F, 2.5F)
                    .build(Util.rec_key(Registries.ENTITY_TYPE, "blazen_knight")));

    public static final Supplier<EntityType<SkyScourge>> SKY_SCOURGE = ENTITY_TYPES.register("sky_scourge",
            () -> EntityType.Builder.<SkyScourge>of(SkyScourge::new, MobCategory.MONSTER)
                    .sized(10F, 5F)
                    .build(Util.rec_key(Registries.ENTITY_TYPE, "sky_scourge")));

    public static final Supplier<EntityType<ForestGuardian>> FOREST_GUARDIAN = ENTITY_TYPES.register("forest_guardian",
            () -> EntityType.Builder.<ForestGuardian>of(ForestGuardian::new, MobCategory.MONSTER)
                    .sized(0.9F, 2F)
                    .build(Util.rec_key(Registries.ENTITY_TYPE, "forest_guardian")));

    public static final Supplier<EntityType<CustomLightning>> CUSTOM_LIGHTNING = ENTITY_TYPES.register("custom_lightning",
            () -> EntityType.Builder.of(CustomLightning::new, MobCategory.MISC)
                    .sized(0.8F, 2F)
                    .build(Util.rec_key(Registries.ENTITY_TYPE, "custom_lightning")));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
