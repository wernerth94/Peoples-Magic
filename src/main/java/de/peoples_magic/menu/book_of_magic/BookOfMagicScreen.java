package de.peoples_magic.menu.book_of_magic;

import com.mojang.blaze3d.systems.RenderSystem;
import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.menu.LargeTextField;
import de.peoples_magic.menu.book_of_magic.tooltips.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class BookOfMagicScreen extends AbstractContainerScreen<BookOfMagicMenu> {
    static final int TEXT_COLOR = ChatFormatting.WHITE.getColor();
    static final Font FONT = Minecraft.getInstance().font;
    private static final ResourceLocation BG_TEXTURE = Util.rec_loc("textures/book_of_magic/book_menu_background.png");
    public final int menu_width = 260;
    public final int menu_height = 179;
    public final int menu_top = 15;
    private final int menu_item_height = 14;

    public int menu_left;
    private int right_menu_left;
    private int first_menu_item, second_menu_item, third_menu_item, fourth_menu_item,
            fifth_menu_item, sixth_menu_item, seventh_menu_item;
    private List<GuiEventListener> active_widgets;
    private BookButton header;
    private Player player;
    private SpellArea spell_area;

    public BookOfMagicScreen(BookOfMagicMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        active_widgets = new ArrayList<>();
        player = playerInventory.player;
        spell_area = new SpellArea();
    }

    @Override
    protected void init() {
        super.init();
        int window_width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        menu_left = (window_width - menu_width) / 2;
        right_menu_left = menu_left + (menu_width / 2) + 9;
        titleLabelX = -100;
        titleLabelY = -100;
        inventoryLabelX = -100;
        inventoryLabelY = -100;
        int menu_item_margin = 4;
        int header_menu_item = menu_top+11;
        first_menu_item = header_menu_item + menu_item_height + 2 * menu_item_margin;
        second_menu_item = first_menu_item + menu_item_height + menu_item_margin;
        third_menu_item = second_menu_item + menu_item_height + menu_item_margin;
        fourth_menu_item = third_menu_item + menu_item_height + menu_item_margin;
        fifth_menu_item = fourth_menu_item + menu_item_height + menu_item_margin;
        sixth_menu_item = fifth_menu_item + menu_item_height + menu_item_margin;
        seventh_menu_item = sixth_menu_item + menu_item_height + menu_item_margin;

        // Header
        set_header("Book of Magic", "textures/book_of_magic/book_of_magic.png");

        String active_menu = player.getData(ModAttachments.BOM_ACTIVE_MENU);
        switch (active_menu) {
            case "spells":
                spells_menu();
                break;
            case "spells_2":
                spells_2_menu();
                break;
            case "spell_absorption":
                spell_absorption_menu();
                break;
            case "spell_fireball":
                spell_fireball_menu();
                break;
            case "spell_ice_cone":
                spell_ice_cone_menu();
                break;
            case "spell_aether_grip":
                spell_aether_grip_menu();
                break;
            case "spell_repel":
                spell_repel_menu();
                break;
            case "spell_haste":
                spell_haste_menu();
                break;
            case "spell_farming":
                spell_farming_menu();
                break;
            case "spell_summon_ally":
                spell_summon_ally_menu();
                break;
            case "mana":
                mana_menu();
                break;
            case "mana_items":
                mana_items_menu();
                break;
            case "learning_spells":
                learning_spells_menu();
                break;
            case "mastering_spells":
                mastering_spells_menu();
                break;
            case "main":
            default:
                main_menu();
        }

        this.addRenderableWidget(spell_area);
        spell_area.init(player, menu_left + 12, menu_top + 10, 110, 154, this);
    }

    private void main_menu() {
        drop_previous_menu();
        set_header("Book of Magic", "textures/item/book_of_magic.png");

        add_button("Mana", Util.rec_loc("textures/book_of_magic/mana_potion_icon.png"),
                right_menu_left, first_menu_item, 110, menu_item_height,
                button -> mana_menu());

        add_button("Mana Items", Util.rec_loc("textures/book_of_magic/mana_pool_icon.png"),
                right_menu_left, second_menu_item, 110, menu_item_height,
                button -> mana_items_menu());

        add_button("Learning Spells", Util.rec_loc("textures/item/tome_of_fireball.png"),
                right_menu_left, third_menu_item, 110, menu_item_height,
                button -> learning_spells_menu());

        add_button("Mastering Spells", Util.rec_loc("textures/book_of_magic/empty.png"),
                right_menu_left, fourth_menu_item, 110, menu_item_height,
                button -> mastering_spells_menu());

        add_button("Spells", Util.rec_loc("textures/book_of_magic/empty.png"),
                right_menu_left, fifth_menu_item, 110, menu_item_height,
                button -> spells_menu());

        player.setData(ModAttachments.BOM_ACTIVE_MENU.get(), "main");
    }


    private void set_header(String text, String icon) {
        if (this.header != null) {
            removeWidget(this.header);
        }
        this.header = new BookButton.Builder(Component.literal(text), button -> main_menu(), false)
                .sprite(Util.rec_loc(icon), 10, 10)
                .size(110, 14).build();
        this.header.setPosition(right_menu_left, menu_top+11);
        this.addRenderableWidget(this.header);
    }


    private void mana_menu() {
        drop_previous_menu();
        set_header("Mana", "textures/book_of_magic/mana_potion_icon.png");
        LargeTextField t = new LargeTextField(right_menu_left, first_menu_item, right_menu_left+110,
                "This mod grants you mana as a resource that you can use to cast spells. By itself, mana only regenerates " +
                        "very slowly, so be careful what you use it for. See 'Mana Items' for ways to increase your mana generation.");
        active_widgets.add(t);
        addRenderableOnly(t);
        player.setData(ModAttachments.BOM_ACTIVE_MENU.get(), "mana");
    }


    private void mana_items_menu() {
        drop_previous_menu();
        set_header("Mana Items", "textures/book_of_magic/mana_pool_icon.png");
        LargeTextField t = new LargeTextField(right_menu_left, first_menu_item, right_menu_left+110,
                "To bolster your mana generation, you can enchant your armor to increase your maximum mana or your mana regeneration, " +
                        "or you can craft a 'Mana Well' that passively collects mana for you.  " +
                        "You can harvest its mana by right-clicking the pool. You can also right-click a pool with an empty " +
                        "bottle. If the pool contains at least 50 mana, it creates a mana potion that you can take with you on " +
                        "your adventures.");
        active_widgets.add(t);
        addRenderableOnly(t);
        player.setData(ModAttachments.BOM_ACTIVE_MENU.get(), "mana_items");
    }


    private void mastering_spells_menu() {
        drop_previous_menu();
        set_header("Mastering Spells", "textures/book_of_magic/empty.png");
        LargeTextField t = new LargeTextField(right_menu_left, first_menu_item, right_menu_left+110,
                "After just learning a spell, it is quite weak. You need to train yourself in this spell by " +
                        "archiving specific objectives with each spell. You can find details about this in the " +
                        "description of each spell. Each spell can be trained until level 7, after which you unlock " +
                        "the 'EXPERTISE' in this spell. Each spell has multiple expertises that alter it's behavior, so " +
                        "that you can customize the spell to your liking."
                        );
        active_widgets.add(t);
        addRenderableOnly(t);
        player.setData(ModAttachments.BOM_ACTIVE_MENU.get(), "mastering_spells");
    }


    private void learning_spells_menu() {
        drop_previous_menu();
        set_header("Learning Spells", "textures/item/tome_of_fireball.png");

        LargeTextField t = new LargeTextField(right_menu_left, first_menu_item, right_menu_left+110,
                "You learn spells by finding and reading spell tomes. Reading a tome consumes it. " +
                        "While structures like buried treasure have a chance to contain " +
                        "a random tome, all tomes have a dedicated place where you can find them: " +
                        "Absorption in mine shafts, Fireball in fortresses and bastions, Ice Cone in shipwrecks, Aether Grip in end cities, " +
                        "Haste in jungle temples and desert pyramids, Farming in underwater ruins, Repel in woodland mansions or pillager outputs, " +
                        "and Summon Ally in ancient cities.");
        active_widgets.add(t);
        addRenderableOnly(t);
        player.setData(ModAttachments.BOM_ACTIVE_MENU.get(), "learning_spells");
    }


    private void spells_menu() {
        drop_previous_menu();
        set_header("Spells", "textures/book_of_magic/empty.png");

        add_button("Absorption", Util.rec_loc("textures/book_of_magic/absorption_icon.png"),
                right_menu_left, first_menu_item, 110, menu_item_height,
                button -> spell_absorption_menu());
        add_button("Fireball", Util.rec_loc("textures/book_of_magic/fireball_icon.png"),
                right_menu_left, second_menu_item, 110, menu_item_height,
                button -> spell_fireball_menu());
        add_button("Ice Cone", Util.rec_loc("textures/book_of_magic/ice_cone_icon.png"),
                right_menu_left, third_menu_item, 110, menu_item_height,
                button -> spell_ice_cone_menu());
        add_button("Aether Grip", Util.rec_loc("textures/book_of_magic/aether_grip_icon.png"),
                right_menu_left, fourth_menu_item, 110, menu_item_height,
                button -> spell_aether_grip_menu());
        add_button("Repel", Util.rec_loc("textures/book_of_magic/repel_icon.png"),
                right_menu_left, fifth_menu_item, 110, menu_item_height,
                button -> spell_repel_menu());
        add_button("Haste", Util.rec_loc("textures/book_of_magic/haste_icon.png"),
                right_menu_left, sixth_menu_item, 110, menu_item_height,
                button -> spell_haste_menu());
        add_button("More Spells", Util.rec_loc("textures/book_of_magic/empty.png"),
                right_menu_left, seventh_menu_item, 110, menu_item_height,
                button -> spells_2_menu());

        player.setData(ModAttachments.BOM_ACTIVE_MENU.get(), "spells");
    }

    private void spells_2_menu() {
        drop_previous_menu();
        set_header("Spells", "textures/book_of_magic/empty.png");

        add_button("Summon Ally", Util.rec_loc("textures/book_of_magic/summon_ally_icon.png"),
                right_menu_left, first_menu_item, 110, menu_item_height,
                button -> spell_summon_ally_menu());
        add_button("Farming", Util.rec_loc("textures/book_of_magic/cow_icon.png"),
                right_menu_left, second_menu_item, 110, menu_item_height,
                button -> spell_farming_menu());



        add_button("Back", Util.rec_loc("textures/book_of_magic/empty.png"),
                right_menu_left, seventh_menu_item, 110, menu_item_height,
                button -> spells_menu());

        player.setData(ModAttachments.BOM_ACTIVE_MENU.get(), "spells_2");
    }


    private void spell_repel_menu() {
        drop_previous_menu();
        set_header("Repel", "textures/book_of_magic/repel_icon.png");
        LargeTextField t = new LargeTextField(right_menu_left, first_menu_item, right_menu_left+110,
                "You create an invisible zone around you that pushes every entity outwards. " +
                        "While repel is active no mob can come close to you. It is leveled by repelling " +
                        "spiders that are jumping at you (mid air).\n\n" +
                        "Expertises:\n" +
                        "Statis / Lightning Storm");
        active_widgets.add(t);
        addRenderableOnly(t);
        player.setData(ModAttachments.BOM_ACTIVE_MENU.get(), "spell_repel");
    }

    private void spell_haste_menu() {
        drop_previous_menu();
        set_header("Haste", "textures/book_of_magic/haste_icon.png");
        LargeTextField t = new LargeTextField(right_menu_left, first_menu_item, right_menu_left+110,
                "You speed yourself up. This affects movement speed as well as mining speed. " +
                        "You can toggle haste on or off and it consumes mana continuously. " +
                        "Haste is leveled by keeping it up for extended periods of time.\n\n" +
                        "Expertises:\n" +
                        "Rush / Fortune");
        active_widgets.add(t);
        addRenderableOnly(t);
        player.setData(ModAttachments.BOM_ACTIVE_MENU.get(), "spell_haste");
    }

    private void spell_farming_menu() {
        drop_previous_menu();
        set_header("Farming", "textures/book_of_magic/cow_icon.png");
        LargeTextField t = new LargeTextField(right_menu_left, first_menu_item, right_menu_left+110,
                "Allows you to sometimes double to outcome of breeding processes. " +
                        "Can be toggled on of or off, but only consumes mana when breeding was affected. " +
                        "Farming is leveled by breeding animals.\n\n" +
                        "Expertises:\n" +
                        "Animals / Crops");
        active_widgets.add(t);
        addRenderableOnly(t);
        player.setData(ModAttachments.BOM_ACTIVE_MENU.get(), "spell_farming");
    }

    private void spell_summon_ally_menu() {
        drop_previous_menu();
        set_header("Summon Ally", "textures/book_of_magic/summon_ally_icon.png");
        LargeTextField t = new LargeTextField(right_menu_left, first_menu_item, right_menu_left+110,
                "Allows you to summon a friendly mob to aid you in battle. It will follow you around and " +
                        "attack monsters in the vicinity. On early levels, you summon a zombie, on later levels " +
                        "you summon skeletons. Summon Ally is leveled by summoning many allies.\n\n" +
                        "Expertises:\n" +
                        "Army / Might / Steed");
        active_widgets.add(t);
        addRenderableOnly(t);
        player.setData(ModAttachments.BOM_ACTIVE_MENU.get(), "spell_summon_ally");
    }

    private void spell_absorption_menu() {
        drop_previous_menu();
        set_header("Absorption", "textures/book_of_magic/absorption_icon.png");
        LargeTextField t = new LargeTextField(right_menu_left, first_menu_item, right_menu_left+110,
                "Absorption grants you golden hearts for a short duration that mitigate incoming damage of any kind " +
                        "(This includes fall damage). It is leveled by using it often and mitigating damage.\n\n" +
                        "Expertises:\n" +
                        "Pillow Fort / Thorns / Dash");
        active_widgets.add(t);
        addRenderableOnly(t);
        player.setData(ModAttachments.BOM_ACTIVE_MENU.get(), "spell_absorption");
    }


    private void spell_fireball_menu() {
        drop_previous_menu();
        set_header("Fireball", "textures/book_of_magic/fireball_icon.png");
        LargeTextField t = new LargeTextField(right_menu_left, first_menu_item, right_menu_left+110,
                "You shoot a single fireball that explodes on impact. Hitting a target directly does extra damage. " +
                        "You level Fireball by directly hitting targets.\n\n" +
                        "Expertises:\n" +
                        "Void / Puncture / Rapid Fire");
        active_widgets.add(t);
        addRenderableOnly(t);
        player.setData(ModAttachments.BOM_ACTIVE_MENU.get(), "spell_fireball");
    }


    private void spell_ice_cone_menu() {
        drop_previous_menu();
        set_header("Ice Cone", "textures/book_of_magic/ice_cone_icon.png");
        LargeTextField t = new LargeTextField(right_menu_left, first_menu_item, right_menu_left+110,
                "You shoot multiple particles that slightly damage and slow targets. " +
                        "You level Ice Cone by hitting multiple targets at once.\n\n" +
                        "Expertises:\n" +
                        "Absolute Zero / Frozen Wave");
        active_widgets.add(t);
        addRenderableOnly(t);
        player.setData(ModAttachments.BOM_ACTIVE_MENU.get(), "spell_ice_cone");
    }


    private void spell_aether_grip_menu() {
        drop_previous_menu();
        set_header("Aether Grip", "textures/book_of_magic/aether_grip_icon.png");
        LargeTextField t = new LargeTextField(right_menu_left, first_menu_item, right_menu_left+110,
                "You create a spectral hand that damages targets in a line and pulls them in your direction upon returning. " +
                        "You level Aether Grip by pulling Skeletons with it.\n\n" +
                        "Expertises:\n" +
                        "Abduction / Homing Missile");
        active_widgets.add(t);
        addRenderableOnly(t);
        player.setData(ModAttachments.BOM_ACTIVE_MENU.get(), "spell_aether_grip");
    }


    private void add_button(String text, ResourceLocation sprite,
                            int x, int y, int width, int height,
                            Button.OnPress on_press) {

        BookButton t = new BookButton.Builder(Component.literal(text),on_press, false)
                .sprite(sprite, 10, 10)
                .size(width, height).build();
        t.setPosition(x, y);
        active_widgets.add(t);
        addRenderableWidget(t);
    }

    private void drop_previous_menu() {
        for (GuiEventListener widget : active_widgets) {
            removeWidget(widget);
        }
        active_widgets.clear();
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (spell_area.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return spell_area.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        for (GuiEventListener widget : this.active_widgets) {
            if (widget instanceof LargeTextField) {
                widget.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
            }
        }
        return spell_area.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        spell_area.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        spell_area.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        guiGraphics.blit(RenderType::guiTextured, BG_TEXTURE, menu_left, menu_top, 0, 0, menu_width, menu_height, menu_width, menu_height);
    }

}
