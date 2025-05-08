package de.peoples_magic.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import de.peoples_magic.Config;
import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.attributes.ModAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ManaBarOverlay implements LayeredDraw.Layer {
    public static final ManaBarOverlay instance = new ManaBarOverlay();
    static final int TEXT_COLOR = ChatFormatting.WHITE.getColor();
    public final static ResourceLocation TEXTURE = Util.rec_loc("textures/gui/mana_bar_icons.png");


    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;
        if (Minecraft.getInstance().options.hideGui || player.isSpectator()) {
            return;
        }
        double player_mana = player.getData(ModAttachments.PLAYER_MANA.get());
        double percentile = player_mana / player.getAttributeValue(ModAttributes.MAX_MANA);
        int y_offset = 5;
        int x_offset = 10;
        int display_height = guiGraphics.guiHeight();
        int display_width = guiGraphics.guiWidth();
        int texture_height = 76;
        int texture_width = 100;
        int bar_height = 17;
        int bar_width = texture_width;
        int adjusted_bar_width = (int) (bar_width * percentile);
        int bar_x = x_offset;
        int bar_y = display_height - bar_height - y_offset;

//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
        // Background
        guiGraphics.blit(RenderType::guiTextured, TEXTURE, bar_x, bar_y, 0, 59, bar_width, bar_height, texture_width, texture_height);
        // Bar Content
        guiGraphics.blit(RenderType::guiTextured, TEXTURE, bar_x, bar_y, 0, 20, adjusted_bar_width, bar_height, texture_width, texture_height);
//        RenderSystem.disableBlend();


        int textX = x_offset + bar_width / 2;
        int textY = display_height - bar_height - y_offset - 9;
        String manaFraction = ((int) player_mana) + "/" + ((int) player.getAttributeValue(ModAttributes.MAX_MANA));
        Font font = Minecraft.getInstance().font;
        guiGraphics.drawString(font, manaFraction, textX, textY, TEXT_COLOR);
    }
}
