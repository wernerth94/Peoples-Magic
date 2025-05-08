package de.peoples_magic.menu.book_of_magic.tooltips;

import de.peoples_magic.Config;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.menu.book_of_magic.BOMTooltip;
import de.peoples_magic.menu.book_of_magic.SpellTile;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class RepelTooltip extends BOMTooltip implements Renderable {
    private float spiders_repelled;
    private float next_spiders_repelled;
    private float cooldown;
    private float next_cooldown;
    private float range;
    private float next_range;
    private float cost;
    private float next_cost;
    private float duration;
    private float next_duration;

    public RepelTooltip(Player player, SpellTile spell_tile, int z) {
        super(player, spell_tile, z);
        cooldown = Util.get_or_last(Config.repel_cds, spell_tile.level);
        next_cooldown = Util.get_or_last(Config.repel_cds, spell_tile.level+1);
        duration = Util.get_or_last(Config.repel_durations, spell_tile.level);
        next_duration = Util.get_or_last(Config.repel_durations, spell_tile.level+1);
        spiders_repelled = player.getData(ModAttachments.REPEL_SPIDERS_REPELLED.get());
        next_spiders_repelled = Util.get_or_last(Config.repel_progression, spell_tile.level);
        range = 2f + 2f * Util.get_or_last(Config.repel_range, spell_tile.level);
        next_range = 2f + 2f * Util.get_or_last(Config.repel_range, spell_tile.level+1);
        cost = Util.get_or_last(Config.repel_cost, spell_tile.level);
        next_cost = Util.get_or_last(Config.repel_cost, spell_tile.level+1);
    }


    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick,
                       int adj_x, int adj_y, int adj_max_x, int adj_max_y) {
        if (mouseX >= adj_x && mouseX <= adj_max_x &&
            mouseY >= adj_y && mouseY <= adj_max_y) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 90);
            guiGraphics.blit(RenderType::guiTextured, BG_TEXTURE, adj_max_x, adj_max_y, 0, 0, 130, 60, 130, 60);
            guiGraphics.pose().popPose();
            int max_level = Config.repel_progression.size();
            String spell_level = spell_tile.level >= max_level ? "max" : String.valueOf(spell_tile.level);
            String next_spell_level = spell_level.equals("max") ? "max" : String.valueOf(spell_tile.level+1);
            int root_x_left = adj_max_x + 5;
            int root_x_right = adj_max_x + 57;
            int root_y = adj_max_y + 13;
            guiGraphics.pose().pushPose();
            float f = 0.7f;
            guiGraphics.pose().scale(f, f, 1.0f); // Render smaller text
            guiGraphics.pose().translate(0, 0, 101);
            guiGraphics.drawString(FONT, Component.literal("Repel"), (int)((root_x_left + 48)*(1/f)), (int)((root_y-10)*(1/f)), WHITE);
            guiGraphics.pose().popPose();
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(0.5f, 0.5f, 1.0f); // Render smaller text
            guiGraphics.pose().translate(0, 0, 101);
            guiGraphics.drawString(FONT, Component.literal(String.format("Current Level: %s", spell_level)), root_x_left*2, root_y*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Spiders Repelled: %1.0f", spiders_repelled)), root_x_left*2, (root_y+6)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Mana: %2.0f", cost)), root_x_left*2, (root_y+16)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Cooldown: %1.1fs", cooldown)), root_x_left*2, (root_y+22)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Range: %1.1f", range)), root_x_left*2, (root_y+28)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Duration: %1.1fs", duration)), root_x_left*2, (root_y+34)*2, WHITE);

            if (!next_spell_level.equals("max")) {
                guiGraphics.drawString(FONT, Component.literal(String.format("Next Level: %s", next_spell_level)), (root_x_right + 8) * 2, root_y * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("Threshold: %1.0f", next_spiders_repelled)), (root_x_right + 8) * 2, (root_y + 6) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Mana: %2.0f", next_cost)), root_x_right * 2, (root_y + 16) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Cooldown: %1.1fs", next_cooldown)), root_x_right * 2, (root_y + 22) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Range: %1.1f", next_range)), root_x_right * 2, (root_y + 28) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Duration: %1.1fs", next_duration)), root_x_right * 2, (root_y + 34) * 2, WHITE);
            }
            else {
                guiGraphics.drawString(FONT, Component.literal("EXPERTISES"), root_x_right * 2, (root_y + 16) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal("Stasis: Levitate Enemies"), root_x_right * 2, (root_y + 22) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal("Lightning Storm: +50% Mana"), root_x_right * 2, (root_y + 28) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal("   Summon Lightning strikes"), root_x_right * 2, (root_y + 34) * 2, WHITE);
            }
            guiGraphics.pose().popPose();
        }
    }
}
