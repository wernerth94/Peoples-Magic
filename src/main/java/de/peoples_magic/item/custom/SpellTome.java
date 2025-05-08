package de.peoples_magic.item.custom;

import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.overlays.FadingMessageOverlay;
import de.peoples_magic.payloads.sync.DisplayMessagePayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Supplier;

public abstract class SpellTome extends Item {
    protected String spell_verbose_name;
    protected String spell_id;
    protected Supplier<AttachmentType<Integer>> spell_knowledge;

    public SpellTome(Properties properties, String name, String id, Supplier<AttachmentType<Integer>> knowledge) {
        super(properties);
        this.spell_verbose_name = name;
        this.spell_id = id;
        this.spell_knowledge = knowledge;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            ServerPlayer server_player = (ServerPlayer) player;
            int spell_level = server_player.getData(this.spell_knowledge);
            if (spell_level > 0) {
                PacketDistributor.sendToPlayer(server_player, new DisplayMessagePayload(String.format("You have already learned %s", spell_verbose_name), 4000));
//                FadingMessageOverlay.instance.show_message(, 4000);
                return InteractionResult.FAIL;
            }
            else {
                PacketDistributor.sendToPlayer(server_player, new DisplayMessagePayload(String.format("You learned %s", spell_verbose_name), 4000));
//                FadingMessageOverlay.instance.show_message(, 4000);
                Util.update_spell_knowledge(server_player, spell_id, 1);
                itemstack.shrink(1);
                level.playSound(null, player.position().x, player.position().y, player.position().z,
                        SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1, 1);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
