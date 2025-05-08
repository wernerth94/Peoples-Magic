package de.peoples_magic.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.peoples_magic.Config;
import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.SpellUtil;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.Supplier;

public class ResetExpertisesCommand {
    public ResetExpertisesCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("reset_expertises")
                        .requires(source -> source.hasPermission(2))
                        .executes(
                                (context) -> exec(context.getSource().getPlayer())
                        ));
    }

    private int exec(ServerPlayer player){
        for (AttachmentType<Integer> spell : ModAttachments.all_spell_knowledge().values()) {
            player.setData(spell, Math.min(1, player.getData(spell)));
        }
        Util.sync_all_spell_knowledge(player);
        return Command.SINGLE_SUCCESS;
    }
}
