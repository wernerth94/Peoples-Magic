package de.peoples_magic.menu.book_of_magic;

import de.peoples_magic.Util;
import de.peoples_magic.payloads.sync.UpdateKnowledgePayload;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public abstract class BOMExpertise implements Renderable, GuiEventListener {
    protected static final ResourceLocation THORNS_TEXTURE = Util.rec_loc("textures/book_of_magic/exp_thorns.png");
    protected static final ResourceLocation COOLDOWN_TEXTURE = Util.rec_loc("textures/book_of_magic/exp_cooldown.png");
    protected static final ResourceLocation SPEED_TEXTURE = Util.rec_loc("textures/book_of_magic/exp_speed.png");
    protected static final ResourceLocation STATIS_TEXTURE = Util.rec_loc("textures/book_of_magic/exp_stasis.png");
    protected static final ResourceLocation WHEAT_TEXTURE = Util.rec_loc("textures/book_of_magic/exp_wheat.png");
    protected static final ResourceLocation COW_TEXTURE = Util.rec_loc("textures/gui/cow.png");
    protected static final ResourceLocation LIGHTNING_STORM_TEXTURE = Util.rec_loc("textures/book_of_magic/exp_lightning_storm.png");
    protected static final ResourceLocation WARDEN_TEXTURE = Util.rec_loc("textures/book_of_magic/exp_warden.png");
    protected static final ResourceLocation HORSE_TEXTURE = Util.rec_loc("textures/book_of_magic/exp_horse.png");
    protected static final ResourceLocation SWORD_TEXTURE = Util.rec_loc("textures/book_of_magic/exp_sword.png");
    protected static final ResourceLocation PICK_TEXTURE = Util.rec_loc("textures/book_of_magic/exp_pick.png");
    protected static final ResourceLocation ABSOLUTE_ZERO_TEXTURE = Util.rec_loc("textures/book_of_magic/exp_absolute_zero.png");
    protected static final ResourceLocation FROZEN_WAVE_TEXTURE = Util.rec_loc("textures/book_of_magic/exp_frozen_wave.png");
    protected static final ResourceLocation ABDUCTION_TEXTURE = Util.rec_loc("textures/book_of_magic/exp_abduction.png");
    protected static final ResourceLocation HOMING_MISSLE_TEXTURE = Util.rec_loc("textures/book_of_magic/exp_homing_missle.png");
    protected static final ResourceLocation VOID_TEXTURE = Util.rec_loc("textures/book_of_magic/exp_void.png");

    protected static final int WHITE = ChatFormatting.WHITE.getColor();
    protected static final Font FONT = Minecraft.getInstance().font;
    protected Player player;
    protected int knowledge;
    public SpellTile tile;
    protected int z_index;
    private int parent_x;
    private int parent_y;
    private int parent_max_x;
    private int parent_max_y;
    private List<List<Integer>> active_buttons;

    public BOMExpertise(SpellArea spell_area, Player player, SpellTile tile, int z_index) {
        this.player = player;
        this.knowledge = this.player.getData(get_attachment());
        this.tile = tile;
        this.z_index = z_index;
        this.parent_x = spell_area.parent.menu_left;
        this.parent_y = spell_area.parent.menu_top;
        this.parent_max_x = spell_area.x + spell_area.width - 3;
        this.parent_max_y = spell_area.parent.menu_top + spell_area.parent.menu_height;
        this.active_buttons = new ArrayList<>();
    }


    protected void draw_expertise(GuiGraphics guiGraphics,
                                  boolean selected, int x, int y, float zoom, ResourceLocation texture,
                                  int mouseX, int mouseY, List<Component> tooltip) {
        int width = (int) (10 * zoom);
        int height = (int) (10 * zoom);
        if (x > parent_x && x + width < parent_max_x && y > parent_y && y + height < parent_max_y) {
            this.active_buttons.add(List.of(x, y, x + width, y + height));
            guiGraphics.fill(x, y, x + width, y + height, selected ? 0x9Fdeb212 : 0x5F000000);
            guiGraphics.blit(RenderType::guiTextured, texture, x, y, 0, 0, width, height, width, height);
            if (mouseX >= x && mouseX <= x + width &&
                    mouseY >= y && mouseY <= y + height) {
                guiGraphics.renderComponentTooltip(FONT, tooltip, x, y - 2);
            }
        }
    }

    protected void clear_active_buttons() {
        this.active_buttons.clear();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int i = 0; i < active_buttons.size(); i++) {
            List<Integer> button_pos = active_buttons.get(i);
            if (mouseX >= button_pos.get(0) && mouseX <= button_pos.get(2) &&
                mouseY >= button_pos.get(1) && mouseY <= button_pos.get(3)) {
                expertise_selected(i + 1);
                return true;
            }
        }
        return GuiEventListener.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        render(guiGraphics, mouseX, mouseY, partialTick, 0, 0, 1f);
    }

    public abstract void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int adj_x, int adj_y, float zoom);


    public abstract String get_name();

    public abstract AttachmentType<Integer> get_attachment();


    protected void expertise_selected(int expertise_id) {
        if (this.knowledge == 1) {
            this.knowledge = 1 + expertise_id;
            this.player.setData(get_attachment(), this.knowledge);
            PacketDistributor.sendToServer(new UpdateKnowledgePayload(get_name(), player.getData(get_attachment())));
        }
    }

    @Override
    public void setFocused(boolean b) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
