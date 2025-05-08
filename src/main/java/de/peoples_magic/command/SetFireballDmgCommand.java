package de.peoples_magic.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.AbsorptionMitigationPayload;
import de.peoples_magic.payloads.sync.FireballHitPayload;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class SetFireballDmgCommand {
    public SetFireballDmgCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("set_fireball_dmg")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("value", IntegerArgumentType.integer())
                        .executes(
                                (context) -> set_fireball_hit(context.getSource().getPlayer(),
                                        IntegerArgumentType.getInteger(context, "value"))
                        )));
    }

    private int set_fireball_hit(ServerPlayer player, int value){
//        ServerPlayer player = context.getSource().getPlayer();
//        float value = IntegerArgumentType.getInteger(context, "value");
        player.setData(ModAttachments.FIREBALL_HITS, value);
        PacketDistributor.sendToPlayer(player, new FireballHitPayload(value));
        return Command.SINGLE_SUCCESS;
    }
}
