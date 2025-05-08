package de.peoples_magic.overlays;

import de.peoples_magic.PeoplesMagicMod;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FadingMessageOverlay implements LayeredDraw.Layer {
    public static final FadingMessageOverlay instance = new FadingMessageOverlay();
    static final Font FONT = Minecraft.getInstance().font;
    private String message;
    private int displayTime;
    private long startTime;
    private float opacity = 1.0f;

    public void show_message(String message, int displayTimeMs) {
        this.message = message;
        this.displayTime = displayTimeMs;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;

        if (elapsedTime <= displayTime - 100) {
            opacity = 1.0f - (float)elapsedTime / displayTime;
            int color = ((int)(opacity * 255) << 24) | 0xFFFFFF;
            guiGraphics.drawString(FONT, message, (screenWidth / 2) - (int)(message.length() * 2.6f), screenHeight - screenHeight / 4, color);
        }
    }
}