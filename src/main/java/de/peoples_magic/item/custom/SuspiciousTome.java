package de.peoples_magic.item.custom;

import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.overlays.FadingMessageOverlay;
import de.peoples_magic.payloads.sync.DisplayMessagePayload;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SuspiciousTome extends Item implements TooltipProvider {

    public SuspiciousTome(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            ServerPlayer server_player = (ServerPlayer) player;
            itemstack.shrink(1);
            for (AttachmentType<Integer> spell : ModAttachments.all_spell_knowledge().values()) {
                server_player.setData(spell, Math.min(1, server_player.getData(spell)));
            }
            Util.sync_all_spell_knowledge(server_player);
            PacketDistributor.sendToPlayer(server_player, new DisplayMessagePayload("Expertises have been cleared", 4000));
            level.playSound(null, player.position().x, player.position().y, player.position().z,
                    SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1, 1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.literal("This book lets you reset your expertises."));
    }

    @Override
    public void addToTooltip(TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
        // Doesn't work yet, so I am using the deprecated appendHoverText
        consumer.accept(Component.literal("This book lets you reset your expertises."));
    }
}
