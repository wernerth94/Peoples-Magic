package de.peoples_magic.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class UnlearnAllSpellsCommand {
    public UnlearnAllSpellsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("unlearn_all_spells")
                        .requires(source -> source.hasPermission(2))
                        .executes(
                                (context) -> unlearn_all_spells(context.getSource().getPlayer())
                        ));
    }

    private int unlearn_all_spells(ServerPlayer player){
        ModAttachments.all_spell_knowledge().forEach((name, spell) -> {
            player.setData(spell, 0);
        });
        Util.sync_all_spell_knowledge(player);
        return Command.SINGLE_SUCCESS;
    }
}
