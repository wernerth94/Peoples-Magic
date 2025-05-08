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

public class FarmingTooltip extends BOMTooltip implements Renderable {
    private int animals_bred;
    private int threshold;
    private float cooldown;
    private float next_cooldown;
    private int cost;
    private int next_cost;
    private float prob;
    private float next_prob;

    public FarmingTooltip(Player player, SpellTile spell_tile, int z) {
        super(player, spell_tile, z);
        cooldown = Util.get_or_last(Config.farming_cds, spell_tile.level);
        next_cooldown = Util.get_or_last(Config.farming_cds, spell_tile.level+1);
        animals_bred = player.getData(ModAttachments.FARMING_ANIMALS_BRED);
        threshold = Util.get_or_last(Config.farming_progression, spell_tile.level);
        prob = Util.get_or_last(Config.farming_probability, spell_tile.level);
        next_prob = Util.get_or_last(Config.farming_probability, spell_tile.level+1);
        cost = Util.get_or_last(Config.farming_cost, spell_tile.level);
        next_cost = Util.get_or_last(Config.farming_cost, spell_tile.level+1);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick,
                       int adj_x, int adj_y, int adj_max_x, int adj_max_y) {
        if (mouseX >= adj_x && mouseX <= adj_max_x &&
                mouseY >= adj_y && mouseY <= adj_max_y) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 90);
            guiGraphics.blit(RenderType::guiTextured, BG_TEXTURE, adj_max_x, adj_max_y, 0, 0, 115, 55, 115, 55);
            guiGraphics.pose().popPose();
            int max_level = Config.farming_progression.size();
            String spell_level = spell_tile.level >= max_level ? "max" : String.valueOf(spell_tile.level);
            String next_spell_level = spell_level.equals("max") ? "max" : String.valueOf(spell_tile.level+1);
            int root_x_left = adj_max_x + 5;
            int root_x_right = adj_max_x + 53;
            int root_y = adj_max_y + 11;
            guiGraphics.pose().pushPose();
            float f = 0.7f;
            guiGraphics.pose().scale(f, f, 1.0f); // Render smaller text
            guiGraphics.pose().translate(0, 0, 101);
            guiGraphics.drawString(FONT, Component.literal("Farming"), (int)((root_x_left + 37)*(1/f)), (int)((root_y-10)*(1/f)), WHITE);
            guiGraphics.pose().popPose();
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(0.5f, 0.5f, 1.0f); // Render smaller text
            guiGraphics.pose().translate(0, 0, 101);
            guiGraphics.drawString(FONT, Component.literal(String.format("Current Level: %s", spell_level)), root_x_left*2, root_y*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Animals Bred: %d", animals_bred)), root_x_left*2, (root_y+6)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Mana: %d/animal", cost)), root_x_left*2, (root_y+16)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Cooldown: %1.1fs", cooldown)), root_x_left*2, (root_y+22)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Probability: %1.1f", prob)), root_x_left*2, (root_y+28)*2, WHITE);

            if (!next_spell_level.equals("max")) {
                guiGraphics.drawString(FONT, Component.literal(String.format("Next Level: %s", next_spell_level)), (root_x_right + 8) * 2, root_y * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("Threshold: %d", threshold)), (root_x_right + 8) * 2, (root_y + 6) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Mana: %d/animal", next_cost)), root_x_right * 2, (root_y + 16) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Cooldown: %1.1fs", next_cooldown)), root_x_right * 2, (root_y + 22) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Probability: %1.1f", next_prob)), root_x_right * 2, (root_y + 28) * 2, WHITE);
            }
            else {
                guiGraphics.drawString(FONT, Component.literal("EXPERTISES"), root_x_right * 2, (root_y + 16) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal("Animals: chance doubled"), root_x_right * 2, (root_y + 22) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal("Crops: chance doubled"), root_x_right * 2, (root_y + 28) * 2, WHITE);
            }
            guiGraphics.pose().popPose();
        }
    }
}
