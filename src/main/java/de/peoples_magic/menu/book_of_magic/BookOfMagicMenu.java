package de.peoples_magic.menu.book_of_magic;

import de.peoples_magic.menu.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class BookOfMagicMenu extends AbstractContainerMenu {
    public ServerPlayer server_player;

    public BookOfMagicMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        super(ModMenus.BOOK_OF_MAGIC_MENU.get(), containerId);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
