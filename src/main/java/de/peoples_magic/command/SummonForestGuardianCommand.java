package de.peoples_magic.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.peoples_magic.entity.ModEntities;
import de.peoples_magic.entity.mini_boss.ForestGuardian;
import de.peoples_magic.entity.mini_boss.SkyScourge;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;

public class SummonForestGuardianCommand {
    public SummonForestGuardianCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("summon_forest_guardian")
                        .requires(source -> source.hasPermission(2))
                        .executes(
                                (context) -> ex(context.getSource().getPlayer())
                        ));
    }

    private int ex(ServerPlayer player){
        ServerLevel level = (ServerLevel) player.level();
        ForestGuardian z = ModEntities.FOREST_GUARDIAN.get().create(level, EntitySpawnReason.COMMAND);
        z.setPos(player.position().x, player.position().y, player.position().z);
        level.addFreshEntity(z);
        return Command.SINGLE_SUCCESS;
    }
}
