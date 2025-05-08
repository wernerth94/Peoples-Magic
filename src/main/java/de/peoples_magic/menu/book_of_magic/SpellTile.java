package de.peoples_magic.menu.book_of_magic;

import com.mojang.blaze3d.systems.RenderSystem;
import de.peoples_magic.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class SpellTile implements Renderable {
    static final int TEXT_COLOR = ChatFormatting.WHITE.getColor();
    static final Font FONT = Minecraft.getInstance().font;
    public int x, y, max_x, max_y, knowledge, level, z_index;
    public ResourceLocation texture, glow;


    public SpellTile(int x, int y, int max_x, int max_y, int knowledge, int level,
                     ResourceLocation texture, ResourceLocation glow) {
        this.x = x;
        this.y = y;
        this.max_x = max_x;
        this.max_y = max_y;
        this.level = level;
        this.knowledge = knowledge;
        this.texture = texture;
        this.glow = glow;
    }


    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Glow
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(RenderType::guiTextured, glow, x-6, y-6, 0, 0, 32, 32, 32, 32);
//        RenderSystem.disableBlend();
        // Background
        if (knowledge > 0) {
            guiGraphics.fill(x, y, max_x, max_y, 0xFF999790);
        }
        Util.draw_box(guiGraphics, x, y, max_x, max_y, 0x9F404c56);
        // Icon
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(RenderType::guiTextured, texture, x+2, y+2, 0, 0, 16, 16, 16, 16);
//        RenderSystem.disableBlend();
        // Key
        if (knowledge > 0) {
            String spell_level = level >= 7 ? "max" : String.valueOf(level);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(1f, 1f, 0f); // draw this string on background level
            guiGraphics.drawString(FONT, String.format("%s", spell_level), max_x-2, max_y-2, TEXT_COLOR);
            guiGraphics.pose().popPose();
        }
    }
}