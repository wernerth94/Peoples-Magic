package de.peoples_magic;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.attributes.ModAttributes;
import de.peoples_magic.block.ModBlockEntities;
import de.peoples_magic.block.ModBlockStateProperties;
import de.peoples_magic.block.ModBlocks;
import de.peoples_magic.effect.ModEffects;
import de.peoples_magic.enchantment.ModEnchantmentEffects;
import de.peoples_magic.entity.*;
import de.peoples_magic.entity.client.*;
import de.peoples_magic.item.ModCreativeModeTabs;
import de.peoples_magic.item.ModItems;
import de.peoples_magic.keymaps.KeyPressHandler;
import de.peoples_magic.keymaps.PeoplesMagicKeyMaps;
import de.peoples_magic.loottables.ModLootModifiers;
import de.peoples_magic.menu.book_of_magic.BookOfMagicScreen;
import de.peoples_magic.menu.ModMenus;
import de.peoples_magic.overlays.FadingMessageOverlay;
import de.peoples_magic.overlays.LearnedSpellsOverlay;
import de.peoples_magic.payloads.PeoplesMagicPayloadRegistrar;
import de.peoples_magic.overlays.ManaBarOverlay;
import de.peoples_magic.potion.ModPotions;
import de.peoples_magic.sound.ModSounds;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

import static de.peoples_magic.attachments.ModAttachments.PLAYER_MANA;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PeoplesMagicMod.MOD_ID)
public class PeoplesMagicMod
{
    public static final String MOD_ID = "peoples_magic";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PeoplesMagicMod(IEventBus modEventBus, ModContainer modContainer)
    {
        ModBlockStateProperties.load();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);
        ModAttachments.register(modEventBus);
        ModEffects.register(modEventBus);
        ModPotions.register(modEventBus);
        ModMenus.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModAttributes.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEnchantmentEffects.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerScreens);
        modEventBus.addListener(this::onEntityAttributeModification);
        modEventBus.addListener(PeoplesMagicPayloadRegistrar::register);
        modEventBus.addListener(PeoplesMagicKeyMaps::registerBindings);

        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new KeyPressHandler());

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }


    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.BOOK_OF_MAGIC_MENU.get(), BookOfMagicScreen::new);
    }


    @SubscribeEvent
    private void onServerTick(ServerTickEvent.Post event) {
        // tick down cooldown on all connected players
        float reduce_cds_by = 1.0f / 20f; // 20 ticks per second
        for (ServerLevel level : event.getServer().getAllLevels()) {
            for (ServerPlayer player : level.getPlayers(player -> true)) {
                // Sync info to new player instance after death or log in
                if (!player.getData(ModAttachments.PLAYER_SYNCED)) {
                    Util.sync_all_player_data(player);
                    player.setData(ModAttachments.PLAYER_SYNCED, true);
                }
                // Active cooldowns
                Util.tick_down_cooldowns(player, reduce_cds_by);
                // Natural mana regeneration
                Util.update_player_mana(player, player.getData(PLAYER_MANA) + player.getAttributeValue(ModAttributes.MANA_REGEN) / 20F);

                SpellUtil.tick_haste(player);
                SpellUtil.check_homing_missile(player);
                SpellUtil.tick_active_fireball_voids(level);
            }
        }
        SpellUtil.clean_homing_missiles();
    }


    public void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ModAttributes.MAX_MANA);
        event.add(EntityType.PLAYER, ModAttributes.MANA_REGEN);
    }

    @SubscribeEvent
    private void onEntityDamage(LivingDamageEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer server_player) {
            if (server_player.getData(ModAttachments.ABSORPTION_KNOWLEDGE) > 0) {
                long time = System.currentTimeMillis();
                long last_absorption = server_player.getData(ModAttachments.LAST_ABSORPTION_CAST);
                int spell_level = Util.level_from_progression(Config.absorption_progression, server_player.getData(ModAttachments.ABSORPTION_MITIGATION));
                float duration_ms = Util.get_or_last(Config.absorption_durations, spell_level) * 1000f;
                if (time - last_absorption < (long)duration_ms) {
                    float mitigation_amount = event.getReduction(DamageContainer.Reduction.ABSORPTION);
                    SpellUtil.increase_absorption_mitigation(server_player, mitigation_amount);
                }
            }
        }
    }

    @SubscribeEvent
    private void onBabyEntitySpawned(BabyEntitySpawnEvent event) {
        if (event.getCausedByPlayer() != null && event.getCausedByPlayer() instanceof ServerPlayer player) {
            if (SpellUtil.trigger_farming(player, true)) {
                Level level = player.level();
                Animal parent1 = (Animal) event.getParentA();
                AgeableMob cloned = parent1.getBreedOffspring((ServerLevel) level, (Animal)event.getParentB());
                if (cloned != null) {
                    cloned.setPos(parent1.position());
//                    cloned.moveTo(parent1.getX(), parent1.getY(), parent1.getZ(), parent1.getYRot(), parent1.getXRot());
                    cloned.setBaby(true);
                    level.addFreshEntity(cloned);
                }
            }
        }
    }



    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @SubscribeEvent
    public void onPlayerConnect(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide) {
            ServerPlayer server_player = (ServerPlayer) event.getEntity();
            Util.sync_all_player_data(server_player);
            if (!server_player.getData(ModAttachments.BOM_RECEIVED)) {
                ItemStack bom = new ItemStack(ModItems.BOOK_OF_MAGIC.get(), 1);
                server_player.getInventory().add(bom);
                server_player.setData(ModAttachments.BOM_RECEIVED, true);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.getEntity().level().isClientSide) {
            SpellUtil.clear_summoned_entities((ServerPlayer) event.getEntity(), (ServerLevel) event.getEntity().level());
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        // only called for blocks broken by players
        if (!event.getPlayer().level().isClientSide) {
            SpellUtil.check_farming((ServerPlayer) event.getPlayer(), event.getState());
            SpellUtil.check_haste_fortune((ServerPlayer) event.getPlayer(), event.getState());
        }
    }


    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            EntityRenderers.register(ModEntities.FIREBALL_PROJECTILE.get(), FireballRenderer::new);
            EntityRenderers.register(ModEntities.AETHER_GRIP_PROJECTILE.get(), AetherGripRenderer::new);
            EntityRenderers.register(ModEntities.BLAZEN_KNIGHT.get(), BlazenKnightRenderer::new);
            EntityRenderers.register(ModEntities.SKY_SCOURGE.get(), SkyScourgeRenderer::new);
            EntityRenderers.register(ModEntities.FOREST_GUARDIAN.get(), ForestGuardianRenderer::new);
            EntityRenderers.register(ModEntities.CUSTOM_LIGHTNING.get(), CustomLightningBoltRenderer::new);
        }

        @SubscribeEvent
        public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
            event.registerAboveAll(Util.rec_loc("mana_bar_layer"), ManaBarOverlay.instance);
            event.registerAboveAll(Util.rec_loc("learned_spells_layer"), LearnedSpellsOverlay.instance);
            event.registerAboveAll(Util.rec_loc("message_overlay"), FadingMessageOverlay.instance);
        }
    }

}
