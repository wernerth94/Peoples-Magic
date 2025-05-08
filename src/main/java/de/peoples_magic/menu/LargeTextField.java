package de.peoples_magic.menu;

import de.peoples_magic.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LargeTextField implements GuiEventListener, Renderable {
//    protected static final int WHITE = ChatFormatting.DARK_BLUE.getColor();
    protected static final int WHITE = 0x101010;
    protected static final Font FONT = Minecraft.getInstance().font;
    protected static final int MAX_LINES = 14;
    private static final float SCALING = 0.7f;
    private int x, y, max_x, max_y;
    private int current_line;
    private List<Component> lines;
    private float character_width_per_lineheight;

    public LargeTextField(int x, int y, int max_x, String text) {
        this(x, y, max_x, y + 135, text);
    }

    public LargeTextField(int x, int y, int max_x, int max_y, String text) {
        this.x = x;
        this.y = y;
        this.max_x = max_x;
        this.max_y = max_y;
        this.character_width_per_lineheight = 5.7f * SCALING / FONT.lineHeight;
        this.lines = text_to_lines(text, max_x - x);
        this.current_line = 0;
    }

    private List<Component> text_to_lines(String text, int container_width) {
        List<Component> result = new ArrayList<>();
        String[] parts = text.split("\n");
        for (String part : parts) {
            String[] remaining_words = part.split(" ");
            String current_line = "";
            int characters_per_line = (int) Math.floor(container_width / (character_width_per_lineheight * FONT.lineHeight));
            while (remaining_words.length > 0) {
                if (current_line.length() + remaining_words[0].length() > characters_per_line) {
                    result.add(Component.literal(current_line));
                    current_line = "";
                } else {
                    current_line += " " + remaining_words[0];
                    remaining_words = Arrays.copyOfRange(remaining_words, 1, remaining_words.length);
                }
            }
            if (current_line.length() > 0) {
                result.add(Component.literal(current_line));
            }
        }
        return result;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
//        Util.draw_box(guiGraphics, x, y, max_x, max_y, 0xAFdeb212);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(SCALING, SCALING, 1f);
        for (int i=current_line; i < Math.min(lines.size(), MAX_LINES+current_line); i++) {
            guiGraphics.drawString(FONT, lines.get(i), m(x), m(y + ((i-current_line) * FONT.lineHeight)), WHITE, false);
        }
        if (lines.size() > MAX_LINES) {
            guiGraphics.drawString(FONT, Component.literal("           << Scroll >>"),
                    m(x), m(y + (MAX_LINES*FONT.lineHeight)), WHITE, false);
        }
        guiGraphics.pose().popPose();
    }

    private int m(int original) {
        return (int)(original * (1f/SCALING));
    }


    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (mouseX >= x && mouseX <= max_x &&
            mouseY >= y && mouseY <= max_y) {
            current_line += scrollY > 0 ? -1 : 1;
            current_line = Math.min(current_line, this.lines.size() - MAX_LINES);
            current_line = Math.max(current_line, 0);
            return true;
        }
        return GuiEventListener.super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }


    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
