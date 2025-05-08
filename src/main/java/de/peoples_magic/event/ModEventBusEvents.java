package de.peoples_magic.event;

import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.entity.ModEntities;
import de.peoples_magic.entity.client.AetherGripProjectileModel;
import de.peoples_magic.entity.client.FireballProjectileModel;
import de.peoples_magic.entity.mini_boss.BlazenKnight;
import de.peoples_magic.entity.mini_boss.ForestGuardian;
import de.peoples_magic.entity.mini_boss.SkyScourge;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = PeoplesMagicMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FireballProjectileModel.LAYER_LOCATION, FireballProjectileModel::createBodyLayer);
        event.registerLayerDefinition(AetherGripProjectileModel.LAYER_LOCATION, AetherGripProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.BLAZEN_KNIGHT.get(), BlazenKnight.createAttributes().build());
        event.put(ModEntities.SKY_SCOURGE.get(), SkyScourge.createAttributes().build());
        event.put(ModEntities.FOREST_GUARDIAN.get(), ForestGuardian.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.BLAZEN_KNIGHT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.WORLD_SURFACE,
                BlazenKnight::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.SKY_SCOURGE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.WORLD_SURFACE,
                SkyScourge::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.FOREST_GUARDIAN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.WORLD_SURFACE,
                ForestGuardian::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
