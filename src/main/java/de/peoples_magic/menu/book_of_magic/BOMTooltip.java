package de.peoples_magic.menu.book_of_magic;

import de.peoples_magic.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public abstract class BOMTooltip implements Renderable {
    protected static final int WHITE = ChatFormatting.WHITE.getColor();
    protected static final Font FONT = Minecraft.getInstance().font;
    protected static final ResourceLocation BG_TEXTURE = Util.rec_loc("textures/book_of_magic/tooltip_bg.png");
    protected SpellTile spell_tile;
    protected Player player;
    protected int z_index;

    public BOMTooltip(Player p, SpellTile t, int z_index) {
        spell_tile = t;
        player = p;
        this.z_index = z_index;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        render(guiGraphics, mouseX, mouseY, partialTick, 0, 0, 0, 0);
    }

    public abstract void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int adj_x, int adj_y, int adj_max_x, int adj_max_y);
}
