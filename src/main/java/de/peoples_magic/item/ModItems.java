package de.peoples_magic.item;

import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.Util;
import de.peoples_magic.item.custom.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PeoplesMagicMod.MOD_ID);

    public static final DeferredItem<Item> BOOK_OF_MAGIC = ITEMS.register("book_of_magic",
            () -> new BookOfMagic(new Item.Properties().setId(Util.rec_key(Registries.ITEM, "book_of_magic"))));

    public static final DeferredItem<Item> SUSPICIOUS_TOME = ITEMS.register("suspicious_tome",
            () -> new SuspiciousTome(new Item.Properties().setId(Util.rec_key(Registries.ITEM, "suspicious_tome"))));

    public static final DeferredItem<Item> TOME_OF_ABSORPTION = ITEMS.register("tome_of_absorption",
            () -> new TomeOfAbsorption(new Item.Properties().setId(Util.rec_key(Registries.ITEM, "tome_of_absorption"))));

    public static final DeferredItem<Item> TOME_OF_FIREBALL = ITEMS.register("tome_of_fireball",
            () -> new TomeOfFireball(new Item.Properties().setId(Util.rec_key(Registries.ITEM, "tome_of_fireball"))));

    public static final DeferredItem<Item> TOME_OF_ICE_CONE = ITEMS.register("tome_of_ice_cone",
            () -> new TomeOfIceCone(new Item.Properties().setId(Util.rec_key(Registries.ITEM, "tome_of_ice_cone"))));

    public static final DeferredItem<Item> TOME_OF_AETHER_GRIP = ITEMS.register("tome_of_aether_grip",
            () -> new TomeOfAetherGrip(new Item.Properties().setId(Util.rec_key(Registries.ITEM, "tome_of_aether_grip"))));

    public static final DeferredItem<Item> TOME_OF_FARMING = ITEMS.register("tome_of_farming",
            () -> new TomeOfFarming(new Item.Properties().setId(Util.rec_key(Registries.ITEM, "tome_of_farming"))));

    public static final DeferredItem<Item> TOME_OF_REPEL = ITEMS.register("tome_of_repel",
            () -> new TomeOfRepel(new Item.Properties().setId(Util.rec_key(Registries.ITEM, "tome_of_repel"))));

    public static final DeferredItem<Item> TOME_OF_SUMMON_ALLY = ITEMS.register("tome_of_summon_ally",
            () -> new TomeOfSummonAlly(new Item.Properties().setId(Util.rec_key(Registries.ITEM, "tome_of_summon_ally"))));

    public static final DeferredItem<Item> TOME_OF_HASTE = ITEMS.register("tome_of_haste",
            () -> new TomeOfHaste(new Item.Properties().setId(Util.rec_key(Registries.ITEM, "tome_of_haste"))));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
