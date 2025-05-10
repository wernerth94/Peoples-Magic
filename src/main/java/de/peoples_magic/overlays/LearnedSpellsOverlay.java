package de.peoples_magic.overlays;

import com.mojang.blaze3d.platform.InputConstants;
import de.peoples_magic.Config;
import de.peoples_magic.SpellUtil;
import de.peoples_magic.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import static de.peoples_magic.attachments.ModAttachments.*;
import static de.peoples_magic.keymaps.PeoplesMagicKeyMaps.*;

@OnlyIn(Dist.CLIENT)
public class LearnedSpellsOverlay implements LayeredDraw.Layer {
    public static final LearnedSpellsOverlay instance = new LearnedSpellsOverlay();
    static final int TEXT_COLOR = ChatFormatting.WHITE.getColor();
    static final int MANA_COLOR = ChatFormatting.AQUA.getColor();
    static final Font FONT = Minecraft.getInstance().font;
    private static final int tile_width = 20;
    private static final int tile_height = 20;
    private static final int y_margin = 5;
    private static final int x_margin = 10;
    private static final int tile_margin = 4;
    private static final int min_x_for_new_line = 100;
    private long fireball_cd_start_time;
    private long ice_cone_cd_start_time;
    private long aether_grip_cd_start_time;
    private long absorption_cd_start_time;
    private long repel_cd_start_time;
    private long haste_cd_start_time;
    private long farming_cd_start_time;
    private long summon_ally_cd_start_time;

    public void active_cd_flash(String spell_name) {
        switch (spell_name) {
            case "fireball":
                this.fireball_cd_start_time = System.currentTimeMillis();
                break;
            case "ice_cone":
                this.ice_cone_cd_start_time = System.currentTimeMillis();
                break;
            case "aether_grip":
                this.aether_grip_cd_start_time = System.currentTimeMillis();
                break;
            case "absorption":
                this.absorption_cd_start_time = System.currentTimeMillis();
                break;
            case "repel":
                this.repel_cd_start_time = System.currentTimeMillis();
                break;
            case "haste":
                this.haste_cd_start_time = System.currentTimeMillis();
                break;
            case "farming":
                this.farming_cd_start_time = System.currentTimeMillis();
                break;
            case "summon_ally":
                this.summon_ally_cd_start_time = System.currentTimeMillis();
                break;
            default:
                throw new IllegalArgumentException("Unknown spell name: " + spell_name);
        }
    }

    private record Tile(int x, int y, int max_x, int max_y, int level, float cd_percentage, long cd_flash_time, String key, Number cost,
                        ResourceLocation texture, boolean is_active) {
        private void draw(GuiGraphics guiGraphics, boolean show_cd) {
            // Background
            guiGraphics.fill(x, y, max_x, max_y, 0x99000000);
            // Icon
//            RenderSystem.enableBlend();
//            RenderSystem.defaultBlendFunc();
            guiGraphics.blit(RenderType::guiTextured, texture, x+2, y+2, 0, 0, 16, 16, 16, 16);
//            RenderSystem.disableBlend();
            // Is active
            if (is_active) {
                Util.draw_box(guiGraphics, x, y, max_x, max_y, 0xFFfac825);
            }
            // Cooldown
            if (show_cd) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - cd_flash_time;
                int color;
                if (elapsedTime <= 400) {
                    color = 0xFFFF0000;
                    int alpha = (int)(1f - (elapsedTime / 400.0) * 0xFF);
                    color = (color & 0x00FFFFFF) | ((alpha & 0xFF) << 24);
                }
                else {
                    color = 0x99FFFFFF;
                }
                int cd_y = (int) (y + (max_y - y) * (1.0 - cd_percentage));
                guiGraphics.fill(x, cd_y, max_x, max_y, color);
            }
            // Key
            if (key.length() > 1) {
                guiGraphics.drawString(FONT, key, max_x-10, max_y-5, TEXT_COLOR);
            }
            else {
                guiGraphics.drawString(FONT, key, max_x-5, max_y-5, TEXT_COLOR);
            }
            // Cost
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(0.5f, 0.5f, 1f);
            if (cost instanceof Integer) {
                guiGraphics.drawString(FONT, String.format("%d", cost), x*2, (max_y-4)*2, MANA_COLOR);
            } else if (cost instanceof Float) {
                guiGraphics.drawString(FONT, String.format("%1.1f", cost), x*2, (max_y-4)*2, MANA_COLOR);
            }
            else {
                guiGraphics.drawString(FONT, String.format("%s", cost), x*2, (max_y-4)*2, MANA_COLOR);
            }
            guiGraphics.pose().popPose();
        }
    }


    private Tile add_tile(GuiGraphics guiGraphics, int level, float cd_percentage, long cd_flash_time, String key, Number cost,
                          ResourceLocation texture, boolean is_active, @Nullable Tile last_tile) {
        int display_height = guiGraphics.guiHeight();
        int display_width = guiGraphics.guiWidth();
        if (last_tile == null) {
            return new Tile(display_width-x_margin-tile_width, display_height-y_margin-tile_height,
                         display_width-x_margin,display_height-y_margin,
                                level, cd_percentage, cd_flash_time, key, cost, texture, is_active);
        }
        if (last_tile.x <= display_width-min_x_for_new_line) {
            return new Tile(display_width-x_margin-tile_width, last_tile.y-tile_height-tile_margin,
                         display_width-x_margin, last_tile.y-tile_margin,
                                level, cd_percentage, cd_flash_time, key, cost, texture, is_active);
        }
        else {
            return new Tile(last_tile.x-tile_margin-tile_width, last_tile.y,
                         last_tile.x-tile_margin, last_tile.max_y,
                                level, cd_percentage, cd_flash_time, key, cost, texture, is_active);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;
        if (Minecraft.getInstance().options.hideGui || player.isSpectator()) {
            return;
        }
        Tile t = null;
        if (player.getData(FIREBALL_KNOWLEDGE.get()) > 0) {
            int level = Util.level_from_progression(Config.fireball_progression, player.getData(FIREBALL_HITS));
            t = add_tile(guiGraphics, level, player.getData(FIREBALL_ACTIVE_CD) / Util.get_or_last(Config.fireball_cds, level),
                    fireball_cd_start_time,
                    key_name(SPELL_FIREBALL_MAPPING),
                    Util.get_or_last(Config.fireball_cost, level),
                    Util.rec_loc("textures/gui/fireball_icon.png"), false, null);
            t.draw(guiGraphics, true);
        }

        if (player.getData(ICE_CONE_KNOWLEDGE.get()) > 0) {
            int level = SpellUtil.get_ice_cone_level(player);
            t = add_tile(guiGraphics, level, player.getData(ICE_CONE_ACTIVE_CD) / Util.get_or_last(Config.ice_cone_cds, level),
                    ice_cone_cd_start_time,
                    key_name(SPELL_ICE_CONE_MAPPING),
                    Util.get_or_last(Config.ice_cone_cost, level),
                    Util.rec_loc("textures/gui/ice_cone_icon.png"), false, t);
            t.draw(guiGraphics, true);
        }

        if (player.getData(AETHER_GRIP_KNOWLEDGE.get()) > 0) {
            int level = SpellUtil.get_aether_grip_level(player);
            ResourceLocation texture;
            boolean show_cd;
            if (player.getData(AETHER_GRIP_ACTIVE_ENTITY) > 0) {
                texture = Util.rec_loc("textures/gui/aether_grip_reverse_icon.png");
                show_cd = false;
            }
            else {
                texture = Util.rec_loc("textures/gui/aether_grip_icon.png");
                show_cd = true;
            }
            t = add_tile(guiGraphics, level, player.getData(AETHER_GRIP_ACTIVE_CD) / Util.get_or_last(Config.aether_grip_cds, level),
                    aether_grip_cd_start_time,
                    key_name(SPELL_AETHER_GRIP_MAPPING),
                    Util.get_or_last(Config.aether_grip_cost, level),
                    texture, false, t);
            t.draw(guiGraphics, show_cd);
        }

        if (player.getData(ABSORPTION_KNOWLEDGE.get()) > 0) {
            int level = Util.level_from_progression(Config.absorption_progression, player.getData(ABSORPTION_MITIGATION));
            t = add_tile(guiGraphics, level,  player.getData(ABSORPTION_ACTIVE_CD) / Util.get_or_last(Config.absorption_cds, level),
                    absorption_cd_start_time,
                    key_name(SPELL_ABSORPTION_MAPPING),
                    Util.get_or_last(Config.absorption_cost, level),
                    Util.rec_loc("textures/gui/absorption_icon.png"), false, t);
            t.draw(guiGraphics, true);
        }

        if (player.getData(REPEL_KNOWLEDGE.get()) > 0) {
            int level = Util.level_from_progression(Config.repel_progression, player.getData(REPEL_SPIDERS_REPELLED));
            t = add_tile(guiGraphics, level,  player.getData(REPEL_ACTIVE_CD) / Util.get_or_last(Config.repel_cds, level),
                    repel_cd_start_time,
                    key_name(SPELL_REPEL_MAPPING),
                    Util.get_or_last(Config.repel_cost, level),
                    Util.rec_loc("textures/gui/repel_icon.png"), false, t);
            t.draw(guiGraphics, true);
        }

        if (player.getData(HASTE_KNOWLEDGE.get()) > 0) {
            int level = SpellUtil.get_haste_level(player);
            ResourceLocation texture = Util.rec_loc("textures/gui/haste_icon.png");
            boolean show_cd, is_active;
            if (player.getData(HASTE_IS_ACTIVE)) {
                show_cd = false;
                is_active = true;
            }
            else {
                show_cd = true;
                is_active = false;
            }
            t = add_tile(guiGraphics, level,  player.getData(HASTE_ACTIVE_CD) / Util.get_or_last(Config.haste_cds, level),
                    haste_cd_start_time,
                    key_name(SPELL_HASTE_MAPPING),
                    Util.get_or_last(Config.haste_cost_per_s, level),
                    texture, is_active, t);
            t.draw(guiGraphics, show_cd);
        }

        if (player.getData(FARMING_KNOWLEDGE.get()) > 0) {
            int level = SpellUtil.get_farming_level(player);
            ResourceLocation texture = Util.rec_loc("textures/gui/cow.png");
            boolean show_cd, is_active;
            if (player.getData(FARMING_IS_ACTIVE)) {
                show_cd = false;
                is_active = true;
            }
            else {
                show_cd = true;
                is_active = false;
            }
            t = add_tile(guiGraphics, level,  player.getData(FARMING_ACTIVE_CD) / Util.get_or_last(Config.farming_cds, level),
                    farming_cd_start_time,
                    key_name(SPELL_FARMING_MAPPING),
                    Util.get_or_last(Config.farming_cost, level),
                    texture, is_active, t);
            t.draw(guiGraphics, show_cd);
        }

        if (player.getData(SUMMON_ALLY_KNOWLEDGE.get()) > 0) {
            int level = SpellUtil.get_summon_ally_level(player);
            float max_cd = Util.get_or_last(Config.summon_ally_cds, level);
            if (player.getData(SUMMON_ALLY_KNOWLEDGE.get()) == 3) {
                max_cd *= 2;
            }
            t = add_tile(guiGraphics, level,  player.getData(SUMMON_ALLY_ACTIVE_CD) / max_cd,
                    summon_ally_cd_start_time,
                    key_name(SPELL_SUMMON_ALLY_MAPPING),
                    Util.get_or_last(Config.summon_ally_cost, level),
                    Util.rec_loc("textures/gui/summon_ally_icon.png"), false, t);
            t.draw(guiGraphics, true);
        }

    }

    private String key_name(Lazy<KeyMapping> key) {
        if (key.get().getKey().getType() == InputConstants.Type.KEYSYM) {
            return key.get().getKey().getDisplayName().getString();
        } else if (key.get().getKey().getType() == InputConstants.Type.MOUSE) {
            String name = key.get().getKey().getDisplayName().getString();
            return String.format("M%s", name.substring(name.length()-1));
        }

        return "Invalid";
    }
}
