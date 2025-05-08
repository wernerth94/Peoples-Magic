package de.peoples_magic.menu.book_of_magic;

import com.mojang.blaze3d.systems.RenderSystem;
import de.peoples_magic.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public abstract class BookButton extends Button {

    protected static final WidgetSprites SPRITES = new WidgetSprites(
            Util.rec_loc("widget/button"),
            Util.rec_loc("widget/button_disabled"),
            Util.rec_loc("widget/button_highlighted")
    );

    protected static final ResourceLocation BG_TEXTURE = Util.rec_loc("textures/book_of_magic/button_corners.png");
    protected ResourceLocation sprite;
    protected int spriteWidth;
    protected int spriteHeight;

    BookButton(
            int width,
            int height,
            Component message,
            int spriteWidth,
            int spriteHeight,
            ResourceLocation sprite,
            OnPress onPress,
            @Nullable CreateNarration createNarration
    ) {
        super(0, 0, width, height, message, onPress, createNarration == null ? DEFAULT_NARRATION : createNarration);
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.sprite = sprite;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
//        RenderSystem.enableBlend();
//        RenderSystem.enableDepthTest();
        guiGraphics.pose().pushPose();
        guiGraphics.blit(RenderType::guiTextured, BG_TEXTURE, this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
        guiGraphics.pose().popPose();
        int i = getFGColor();
        this.renderString(guiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    public static Builder builder(Component message, OnPress onPress, boolean iconOnly) {
        return new Builder(message, onPress, iconOnly);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Builder {
        private final Component message;
        private final OnPress onPress;
        private final boolean iconOnly;
        private int width = 150;
        private int height = 20;
        @Nullable
        private ResourceLocation sprite;
        private int spriteWidth;
        private int spriteHeight;
        @Nullable
        CreateNarration narration;

        public Builder(Component message, OnPress onPress, boolean iconOnly) {
            this.message = message;
            this.onPress = onPress;
            this.iconOnly = iconOnly;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder sprite(ResourceLocation sprite, int spriteWidth, int spriteHeight) {
            this.sprite = sprite;
            this.spriteWidth = spriteWidth;
            this.spriteHeight = spriteHeight;
            return this;
        }

        public Builder narration(CreateNarration narration) {
            this.narration = narration;
            return this;
        }

        public BookButton build() {
            if (this.sprite == null) {
                throw new IllegalStateException("Sprite not set");
            } else {
                return (BookButton) (this.iconOnly
                        ? new CenteredIcon(
                        this.width, this.height, this.message, this.spriteWidth, this.spriteHeight, this.sprite, this.onPress, this.narration
                )
                        : new TextAndIcon(
                        this.width, this.height, this.message, this.spriteWidth, this.spriteHeight, this.sprite, this.onPress, this.narration
                ));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class CenteredIcon extends BookButton {
        protected CenteredIcon(
                int p_295914_,
                int p_294852_,
                Component p_295609_,
                int p_294922_,
                int p_296462_,
                ResourceLocation p_295554_,
                OnPress p_294427_,
                @Nullable CreateNarration p_330653_
        ) {
            super(p_295914_, p_294852_, p_295609_, p_294922_, p_296462_, p_295554_, p_294427_, p_330653_);
        }

        @Override
        public void renderWidget(GuiGraphics p_295402_, int p_295733_, int p_294839_, float p_296191_) {
            super.renderWidget(p_295402_, p_295733_, p_294839_, p_296191_);
            int i = this.getX() + this.getWidth() / 2 - this.spriteWidth / 2;
            int j = this.getY() + this.getHeight() / 2 - this.spriteHeight / 2;
            p_295402_.blitSprite(RenderType::guiTextured, this.sprite, i, j, this.spriteWidth, this.spriteHeight);
        }

        @Override
        public void renderString(GuiGraphics p_294683_, Font p_295870_, int p_295770_) {
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class TextAndIcon extends BookButton {
        protected TextAndIcon(
                int p_296442_,
                int p_294340_,
                Component p_296265_,
                int p_294900_,
                int p_295900_,
                ResourceLocation p_296097_,
                OnPress p_295566_,
                @Nullable CreateNarration p_330735_
        ) {
            super(p_296442_, p_294340_, p_296265_, p_294900_, p_295900_, p_296097_, p_295566_, p_330735_);
        }

        @Override
        public void renderWidget(GuiGraphics p_294156_, int p_295818_, int p_294994_, float p_296436_) {
            super.renderWidget(p_294156_, p_295818_, p_294994_, p_296436_);
            int i = this.getX() + this.getWidth() - this.spriteWidth - 6;
            int j = this.getY() + this.getHeight() / 2 - this.spriteHeight / 2;
//            RenderSystem.enableBlend();
//            RenderSystem.defaultBlendFunc();
            p_294156_.blit(RenderType::guiTextured, this.sprite, i, j, 0, 0, this.spriteWidth, this.spriteHeight, this.spriteWidth, this.spriteHeight);
//            RenderSystem.disableBlend();
        }

        @Override
        public void renderString(GuiGraphics p_296211_, Font p_295107_, int p_295081_) {
            int i = this.getX() + 2;
            int j = this.getX() + this.getWidth() - this.spriteWidth - 4;
            int k = this.getX() + this.getWidth() / 2;
            renderScrollingString(p_296211_, p_295107_, this.getMessage(), k, i, this.getY(), j, this.getY() + this.getHeight(), p_295081_);
        }
    }
}

