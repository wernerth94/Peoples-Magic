package de.peoples_magic.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.List;

public class LearnAllSpellsCommand {
    public LearnAllSpellsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("learn_all_spells")
                        .requires(source -> source.hasPermission(2))
                        .executes(
                                (context) -> learn_all_spells(context.getSource().getPlayer())
                        ));
    }

    private int learn_all_spells(ServerPlayer player){
        ModAttachments.all_spell_knowledge().forEach((name, spell) -> {
            player.setData(spell, Math.max(player.getData(spell), 1));
        });
        Util.sync_all_spell_knowledge(player);
        return Command.SINGLE_SUCCESS;
    }
}
