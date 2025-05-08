package de.peoples_magic.menu.book_of_magic.expertises;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.menu.book_of_magic.BOMExpertise;
import de.peoples_magic.menu.book_of_magic.SpellArea;
import de.peoples_magic.menu.book_of_magic.SpellTile;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.List;

public class IceConeExpertise extends BOMExpertise {


    public IceConeExpertise(SpellArea spell_area, Player player, SpellTile tile, int z_index) {
        super(spell_area, player, tile, z_index);
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int adj_x, int adj_y, float zoom) {
        int root_y = adj_y - (int)(8 * zoom);
        int first_x = adj_x - (int)(3 * zoom);
        int second_x = first_x + (int)(12 * zoom);
        int third_x = second_x + (int)(12 * zoom);
        clear_active_buttons();
        draw_expertise(guiGraphics, this.knowledge == 2, first_x, root_y, zoom, ABSOLUTE_ZERO_TEXTURE,
                mouseX, mouseY, List.of(Component.literal("Absolute Zero")));
        draw_expertise(guiGraphics, this.knowledge == 3, second_x, root_y, zoom, FROZEN_WAVE_TEXTURE,
                mouseX, mouseY, List.of(Component.literal("Frozen Wave")));
//        draw_expertise(guiGraphics, this.knowledge == 4, third_x, root_y, zoom, SPEED_TEXTURE,
//                mouseX, mouseY, List.of(Component.literal("Dash")));
    }

    @Override
    public String get_name() {
        return "ice_cone";
    }

    @Override
    public AttachmentType<Integer> get_attachment() {
        return ModAttachments.ICE_CONE_KNOWLEDGE.get();
    }

}
