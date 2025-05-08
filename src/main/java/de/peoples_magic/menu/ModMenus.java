package de.peoples_magic.menu;

import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.menu.book_of_magic.BookOfMagicMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MOD_MENUS = DeferredRegister.create(BuiltInRegistries.MENU, PeoplesMagicMod.MOD_ID);

    public static final Supplier<MenuType<BookOfMagicMenu>> BOOK_OF_MAGIC_MENU = MOD_MENUS.register("book_of_magic_menu",
            () -> IMenuTypeExtension.create(BookOfMagicMenu::new));


    public static void register(IEventBus bus) {
        MOD_MENUS.register(bus);
    }
}
