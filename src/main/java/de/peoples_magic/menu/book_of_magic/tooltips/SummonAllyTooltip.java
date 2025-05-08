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

public class SummonAllyTooltip extends BOMTooltip implements Renderable {
    private float summon_count;
    private float threshold;
    private float cooldown;
    private float next_cooldown;
    private float cost;
    private float next_cost;
    private String type;
    private String next_type;
    private float time_to_live;
    private float next_time_to_live;

    public SummonAllyTooltip(Player player, SpellTile spell_tile, int z) {
        super(player, spell_tile, z);
        cooldown = Util.get_or_last(Config.summon_ally_cds, spell_tile.level);
        next_cooldown = Util.get_or_last(Config.summon_ally_cds, spell_tile.level+1);
        summon_count = player.getData(ModAttachments.SUMMON_ALLY_SUMMONS);
        threshold = Util.get_or_last(Config.summon_ally_progression, spell_tile.level);
        cost = Util.get_or_last(Config.summon_ally_cost, spell_tile.level);
        next_cost = Util.get_or_last(Config.summon_ally_cost, spell_tile.level+1);
        time_to_live = Util.get_or_last(Config.summon_ally_time_to_live, spell_tile.level);
        next_time_to_live = Util.get_or_last(Config.summon_ally_time_to_live, spell_tile.level+1);

        type = get_type(spell_tile.level);
        next_type = get_type(spell_tile.level + 1);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick,
                       int adj_x, int adj_y, int adj_max_x, int adj_max_y) {
        if (mouseX >= adj_x && mouseX <= adj_max_x &&
                mouseY >= adj_y && mouseY <= adj_max_y) {
//            guiGraphics.blitSprite(RenderType::guiTextured, BG_TEXTURE, adj_max_x, adj_max_y, 100, 130, 60);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 90);
            guiGraphics.blit(RenderType::guiTextured, BG_TEXTURE, adj_max_x, adj_max_y, 0, 0, 130, 60, 130, 60);
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
            guiGraphics.drawString(FONT, Component.literal("Summon Ally"), (int)((root_x_left + 35)*(1/f)), (int)((root_y-10)*(1/f)), WHITE);
            guiGraphics.pose().popPose();
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(0.5f, 0.5f, 1.0f); // Render smaller text
            guiGraphics.pose().translate(0, 0, 101);
            guiGraphics.drawString(FONT, Component.literal(String.format("Current Level: %s", spell_level)), root_x_left*2, root_y*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Allies Summoned: %1.0f", summon_count)), root_x_left*2, (root_y+6)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Mana: %2.0f", cost)), root_x_left*2, (root_y+16)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Cooldown: %1.1fs", cooldown)), root_x_left*2, (root_y+22)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Type: %s", type)), root_x_left*2, (root_y+28)*2, WHITE);
            guiGraphics.drawString(FONT, Component.literal(String.format("Time to Live: %ds", (int)time_to_live)), root_x_left*2, (root_y+34)*2, WHITE);

            if (!next_spell_level.equals("max")) {
                guiGraphics.drawString(FONT, Component.literal(String.format("Next Level: %s", next_spell_level)), (root_x_right + 8) * 2, root_y * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("Threshold: %1.0f", threshold)), (root_x_right + 8) * 2, (root_y + 6) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Mana: %2.0f", next_cost)), root_x_right * 2, (root_y + 16) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Cooldown: %1.1fs", next_cooldown)), root_x_right * 2, (root_y + 22) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Type: %s", next_type)), root_x_right * 2, (root_y + 28) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal(String.format("-> Time to Live: %ds", (int)next_time_to_live)), root_x_right * 2, (root_y + 34) * 2, WHITE);
            }
            else {
                guiGraphics.drawString(FONT, Component.literal("EXPERTISES"), root_x_right * 2, (root_y + 10) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal("Army: -50% cooldown"), root_x_right * 2, (root_y + 16) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal("Might: +50% CD +50% Mana"), root_x_right * 2, (root_y + 22) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal("     Summon a warden"), root_x_right * 2, (root_y + 28) * 2, WHITE);
                guiGraphics.drawString(FONT, Component.literal("Steed: Summon a horse"), root_x_right * 2, (root_y + 34) * 2, WHITE);
            }
            guiGraphics.pose().popPose();
        }
    }

    private String get_type(int spell_level) {
        if (spell_level < 3) {
            return "Zombie";
        } else if (spell_level < 6) {
            return "Skeleton";
        }
        else {
            return "Wither Skeleton";
        }
    }
}
