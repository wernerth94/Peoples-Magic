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

public class IceConeTooltip extends BOMTooltip implements Renderable {
    private float multiple_hits;
    private float threshold;
    private float cooldown;
    private float next_cooldown;
    private float cost;
    private float next_cost;
    private float damage;
    private float next_damage;
    private float slowness;
    private float next_slowness;
    private float duration;
    private float next_duration;

    public IceConeTooltip(Player player, SpellTile spell_tile, int z) {
        super(player, spell_tile, z);
        cooldown = Util.get_or_last(Config.ice_cone_cds, spell_tile.level);
        next_cooldown = Util.get_or_last(Config.ice_cone_cds, spell_tile.level+1);
        multiple_hits = player.getData(ModAttachments.ICE_CONE_MULTIPLE_HIT);
        threshold = Util.get_or_last(Config.ice_cone_progression, spell_tile.level);
        slowness = 1+Util.get_or_last(Config.ice_cone_amplifiers, spell_tile.level);
        next_slowness = 1+Util.get_or_last(Config.ice_cone_amplifiers, spell_tile.level+1);
        cost = Util.get_or_last(Config.ice_cone_cost, spell_tile.level);
        next_cost = Util.get_or_last(Config.ice_cone_cost, spell_tile.level+1);
        damage = Util.get_or_last(Config.ice_cone_damages, spell_tile.level);
        next_damage = Util.get_or_last(Config.ice_cone_damages, spell_tile.level+1);
        duration = Util.get_or_last(Config.ice_cone_durations, spell_tile.level);
        next_duration = Util.get_or_last(Config.ice_cone_durations, spell_tile.level+1);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick,
                       int adj_x, int adj_y, int adj_max_x, int adj_max_y) {
        if (mouseX >= adj_x && mouseX <= adj_max_x &&
                mouseY >= adj_y && mouseY <= adj_max_y) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 90);
            guiGraphics.blit(RenderType::guiTextured, BG_TEXTURE, adj_max_x, adj_max_y, 0, 0, 132, 63, 132, 63);
            guiGraphics.pose().popPose();
            int max_level = Config.absorption_progression.size();
            String spell_level = spell_tile.level >= max_level ? "max" : String.valueOf(spell_tile.level);
            String next_spell_level = spell_level.equals("max") ? "max" : String.valueOf(spell_tile.level+1);
            int root_x_left = adj_max_x + 5;
            int root_x_right = adj_max_x + 65;
            int root_y = adj_max_y + 13;
            guiGraphics.pose().pushPose();
            float f = 0.7f;
            guiGraphics.pose().scale(f, f, 1.0f); // Render smaller text
            guiGraphics.pose().translate(0, 0, 101);
            guiGraphics.drawString(FONT, Component.literal("Ice Cone"), (int)((root_x_left + 35)*(1/f)), (int)((root_y-10)*(1/f)), WHITE);
            guiGraphics.pose().popPose();
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(0.5f, 0.5f, 1.0f); // Render smaller text
            guiGraphics.pose().translate(0, 0, 101);
            guiGraphics.drawString(FONT, Component.literal(String.format("Current Level: %s", spell_level)), root_x_left*2, root_y*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Multiple Targets Hit: %1.0f", multiple_hits)), root_x_left*2, (root_y+6)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Mana: %2.0f", cost)), root_x_left*2, (root_y+16)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Cooldown: %1.1fs", cooldown)), root_x_left*2, (root_y+22)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Slowness: %1.1f", slowness)), root_x_left*2, (root_y+28)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Duration: %1.1fs", duration)), root_x_left*2, (root_y+34)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Damage: %1.1f", damage)), root_x_left*2, (root_y+40)*2, WHITE);

            if (!next_spell_level.equals("max")) {
                guiGraphics.drawString(FONT, Component.literal(String.format("Next Level: %s", next_spell_level)), (root_x_right + 8) * 2, root_y * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("Threshold: %1.0f", threshold)), (root_x_right + 8) * 2, (root_y + 6) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Mana: %2.0f", next_cost)), root_x_right * 2, (root_y + 16) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Cooldown: %1.1fs", next_cooldown)), root_x_right * 2, (root_y + 22) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Slowness: %1.1f", next_slowness)), root_x_right * 2, (root_y + 28) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Duration: %1.1fs", next_duration)), root_x_right * 2, (root_y + 34) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Damage: %1.1f", next_damage)), root_x_right * 2, (root_y + 40) * 2, WHITE);
            }
            else {
                guiGraphics.drawString(FONT, Component.literal("EXPERTISES"), root_x_right * 2, (root_y + 16) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal("Absolute Zero: Freezes"), root_x_right * 2, (root_y + 22) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal("        enemies in place"), root_x_right * 2, (root_y + 28) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal("Frozen Wave: Transforms"), root_x_right * 2, (root_y + 34) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal("     the cone into a wave"), root_x_right * 2, (root_y + 40) * 2, WHITE);
            }
            guiGraphics.pose().popPose();
        }
    }
}
