package de.peoples_magic.menu.book_of_magic;

import com.mojang.blaze3d.systems.RenderSystem;
import de.peoples_magic.SpellUtil;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.menu.book_of_magic.expertises.*;
import de.peoples_magic.menu.book_of_magic.tooltips.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class SpellArea implements GuiEventListener, Renderable, NarratableEntry {
    private static final ResourceLocation BG_SPELLS_TEXTURE = Util.rec_loc("textures/book_of_magic/book_spells_background.png");
    private static final ResourceLocation BG_SPELL_DIVIDER = Util.rec_loc("textures/book_of_magic/spell_divider.png");
    private final int tile_size = 20;
    private final int margin = 9;
    public BookOfMagicScreen parent;
    public int x;
    public int y;
    public int width;
    public int height;
    private float zoom;
    private float view_center_x;
    private float view_center_y;
    private float current_view_x;
    private float current_view_y;
    private List<SpellTile> active_spell_tiles;
    private List<BOMTooltip> active_tooltips;
    private List<BOMExpertise> active_expertises;

    public SpellArea() {
        active_spell_tiles = new ArrayList<>();
        active_tooltips = new ArrayList<>();
        active_expertises = new ArrayList<>();
        zoom = 0.7f;
        current_view_x = 35;
        current_view_y = 10;
    }


    public void init(Player player, int x, int y, int width, int height, BookOfMagicScreen parent) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        view_center_x = x + (float)width / 2;
        view_center_y = y + (float)height / 2;
        active_spell_tiles.clear();
        active_tooltips.clear();
        active_expertises.clear();

        SpellTile tile = null;
        BOMTooltip tooltip = null;
        BOMExpertise expertise = null;

        tile = add_tile(x + 72, y + 20, player.getData(ModAttachments.HASTE_KNOWLEDGE), SpellUtil.get_haste_level(player),
                Util.rec_loc("textures/gui/haste_icon.png"), Util.rec_loc("textures/book_of_magic/utility_spell_glow.png"));
        active_spell_tiles.add(tile);
        if (SpellUtil.get_haste_level(player) >= 7) {
            expertise = new HasteExpertise(this, player, tile, 1);
            active_expertises.add(expertise);
        }
        tooltip = new HasteTooltip(player, tile, 100);
        active_tooltips.add(tooltip);

        tile = add_tile(x + 80, y + 65, player.getData(ModAttachments.FARMING_KNOWLEDGE), SpellUtil.get_farming_level(player),
                Util.rec_loc("textures/gui/cow.png"), Util.rec_loc("textures/book_of_magic/utility_spell_glow.png"));
        active_spell_tiles.add(tile);
        if (SpellUtil.get_farming_level(player) >= 7) {
            expertise = new FarmingExpertise(this, player, tile, 1);
            active_expertises.add(expertise);
        }
        tooltip = new FarmingTooltip(player, tile, 100);
        active_tooltips.add(tooltip);

        tile = add_tile(x + 50, y + 110, player.getData(ModAttachments.ABSORPTION_KNOWLEDGE), SpellUtil.get_absorption_level(player),
                Util.rec_loc("textures/gui/absorption_icon.png"), Util.rec_loc("textures/book_of_magic/defensive_spell_glow.png"));
        active_spell_tiles.add(tile);
        if (SpellUtil.get_absorption_level(player) >= 7) {
            expertise = new AbsorptionExpertise(this, player, tile, 1);
            active_expertises.add(expertise);
        }
        tooltip = new AbsorptionTooltip(player, tile, 100);
        active_tooltips.add(tooltip);

        tile = add_tile(x + 10, y + 135, player.getData(ModAttachments.REPEL_KNOWLEDGE), SpellUtil.get_repel_level(player),
                Util.rec_loc("textures/gui/repel_icon.png"), Util.rec_loc("textures/book_of_magic/defensive_spell_glow.png"));
        active_spell_tiles.add(tile);
        if (SpellUtil.get_repel_level(player) >= 7) {
            expertise = new RepelExpertise(this, player, tile, 1);
            active_expertises.add(expertise);
        }
        tooltip = new RepelTooltip(player, tile, 100);
        active_tooltips.add(tooltip);

        tile = add_tile(x + 18, y + 28, player.getData(ModAttachments.FIREBALL_KNOWLEDGE), SpellUtil.get_fireball_level(player),
                Util.rec_loc("textures/gui/fireball_icon.png"), Util.rec_loc("textures/book_of_magic/combat_spell_glow.png"));
        active_spell_tiles.add(tile);
        if (SpellUtil.get_fireball_level(player) >= 7) {
            expertise = new FireballExpertise(this, player, tile, 1);
            active_expertises.add(expertise);
        }
        tooltip = new FireballTooltip(player, tile, 100);
        active_tooltips.add(tooltip);

        tile = add_tile(x - 9, y + 69, player.getData(ModAttachments.ICE_CONE_KNOWLEDGE), SpellUtil.get_ice_cone_level(player),
                Util.rec_loc("textures/gui/ice_cone_icon.png"), Util.rec_loc("textures/book_of_magic/combat_spell_glow.png"));
        active_spell_tiles.add(tile);
        if (SpellUtil.get_ice_cone_level(player) >= 7) {
            expertise = new IceConeExpertise(this, player, tile, 1);
            active_expertises.add(expertise);
        }
        tooltip = new IceConeTooltip(player, tile, 100);
        active_tooltips.add(tooltip);

        tile = add_tile(x - 20, y + 31, player.getData(ModAttachments.AETHER_GRIP_KNOWLEDGE), SpellUtil.get_aether_grip_level(player),
                Util.rec_loc("textures/gui/aether_grip_icon.png"), Util.rec_loc("textures/book_of_magic/combat_spell_glow.png"));
        active_spell_tiles.add(tile);
        if (SpellUtil.get_aether_grip_level(player) >= 7) {
            expertise = new AetherGripExpertise(this, player, tile, 1);
            active_expertises.add(expertise);
        }
        tooltip = new AetherGripTooltip(player, tile, 100);
        active_tooltips.add(tooltip);

        tile = add_tile(x - 20, y - 10, player.getData(ModAttachments.SUMMON_ALLY_KNOWLEDGE), SpellUtil.get_summon_ally_level(player),
                Util.rec_loc("textures/gui/summon_ally_icon.png"), Util.rec_loc("textures/book_of_magic/combat_spell_glow.png"));
        active_spell_tiles.add(tile);
        if (SpellUtil.get_summon_ally_level(player) >= 7) {
            expertise = new SummonAllyExpertise(this, player, tile, 1);
            active_expertises.add(expertise);
        }
        tooltip = new SummonAllyTooltip(player, tile, 100);
        active_tooltips.add(tooltip);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= x && mouseX <= x+width &&
            mouseY >= y && mouseY <= y+height) {
            for (BOMExpertise expertise : active_expertises) {
                if (expertise.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return GuiEventListener.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (mouseX >= x && mouseX <= x+width &&
            mouseY >= y && mouseY <= y+height) {
            current_view_x += dragX;
            current_view_y += dragY;
            return true;
        }
        return GuiEventListener.super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (mouseX >= x && mouseX <= x+width &&
            mouseY >= y && mouseY <= y+height) {
            zoom += 0.1f * (float)scrollY;
            return true;
        }
        return GuiEventListener.super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (BOMTooltip tip : active_tooltips) {
            List<Float> adj_coords = adjust_coordinates(tip.spell_tile);
            if (should_render(tip.spell_tile)) {
                float max_x = Math.min(adj_coords.get(2), this.x + this.width);
                float max_y = Math.min(adj_coords.get(3), this.y + this.height);
                tip.render(guiGraphics, mouseX, mouseY, partialTick,
                           adj_coords.get(0).intValue(), adj_coords.get(1).intValue(), (int)max_x, (int)max_y);
            }
        }

        for (BOMExpertise expertise : active_expertises) {
            List<Float> adj_coords = adjust_coordinates(expertise.tile);
            if (should_render(expertise.tile)) {
                expertise.render(guiGraphics, mouseX, mouseY, partialTick,
                        adj_coords.get(0).intValue(), adj_coords.get(1).intValue(), zoom);
            }
        }
    }

    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RenderType::guiTextured, BG_SPELLS_TEXTURE, x, y, 0, 0, width, height, width, height);
        float x_offset = view_center_x - view_center_x * zoom;
        float y_offset = view_center_y - view_center_y * zoom;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(zoom, zoom, 1f);
        guiGraphics.pose().translate(x_offset, y_offset, 0f);
        guiGraphics.pose().translate(current_view_x, current_view_y, 0f);

//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
        if (should_render(x + 20, y + 40, x + 20 + 64, y + 40 + 64)) {
            guiGraphics.blit(RenderType::guiTextured, BG_SPELL_DIVIDER, x + 20, y + 40, 0, 0, 64, 64, 64, 64);
        }
//        RenderSystem.disableBlend();

        for (SpellTile tile : active_spell_tiles) {
            if (should_render(tile)) {
                tile.render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }
        guiGraphics.pose().popPose();
        Util.draw_box(guiGraphics, x, y, x+width, y+height, 0xAFdeb212);
    }


    private SpellTile add_tile(int x, int y, int spell_knowledge, int spell_level,
                               ResourceLocation texture, ResourceLocation glow) {
        return new SpellTile(x, y, x + tile_size,y + tile_size,
                             spell_knowledge, spell_level, texture, glow);
    }


    private boolean should_render(float x, float y, float max_x, float max_y) {
        List<Float> adj_coords = adjust_coordinates(x, y, max_x, max_y);
        float sx = adj_coords.get(0);
        float sy = adj_coords.get(1);
        float sy_max = adj_coords.get(3) + 4;// account for text outside the tile
        return sx >= parent.menu_left && sx <= this.x + this.width - 3 &&
                sy >= parent.menu_top  && sy_max <= parent.menu_top + parent.menu_height;
    }


    private boolean should_render(SpellTile tile) {
        List<Float> adj_coords = adjust_coordinates(tile);
        float sx = adj_coords.get(0);
        float sy = adj_coords.get(1);
        float sy_max = adj_coords.get(3) + 4;// account for text outside the tile
        return sx >= parent.menu_left && sx <= this.x + this.width - 3 &&
                sy >= parent.menu_top  && sy_max <= parent.menu_top + parent.menu_height;
    }


    private List<Float> adjust_coordinates(float x, float y, float max_x, float max_y) {
        List<Float> result = new ArrayList<>();
        float x_offset = view_center_x - view_center_x * zoom;
        float y_offset = view_center_y - view_center_y * zoom;
        result.add((x + x_offset + current_view_x) * zoom);
        result.add((y + y_offset + current_view_y) * zoom);
        result.add((max_x + x_offset + current_view_x) * zoom);
        result.add((max_y + y_offset + current_view_y) * zoom);
        return result;
    }

    private List<Float> adjust_coordinates(SpellTile tile) {
        return adjust_coordinates(tile.x, tile.y, tile.max_x, tile.max_y);
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }
}
