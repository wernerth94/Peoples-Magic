package de.peoples_magic.item.custom;

import de.peoples_magic.menu.book_of_magic.BookOfMagicMenu;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Consumer;

public class BookOfMagic extends Item implements TooltipProvider {
    public BookOfMagic(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide) {
            if (player instanceof ServerPlayer server_player) {
                server_player.openMenu(new SimpleMenuProvider(
                        (containerId, playerInventory, p) -> new BookOfMagicMenu(containerId, playerInventory, null),
                        Component.translatable("menu.title.peoples_magic.book_of_magic")
                ));
            }
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.literal("This book teaches you about magic."));
    }

    @Override
    public void addToTooltip(TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
        // Doesn't work yet, so I am using the deprecated appendHoverText
        consumer.accept(Component.literal("This book teaches you about magic."));
    }
}
