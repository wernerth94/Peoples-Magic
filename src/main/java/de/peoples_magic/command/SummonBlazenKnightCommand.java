package de.peoples_magic.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.peoples_magic.SpellUtil;
import de.peoples_magic.entity.ModEntities;
import de.peoples_magic.entity.mini_boss.BlazenKnight;
import de.peoples_magic.entity.spells.SummonedSkeleton;
import de.peoples_magic.entity.spells.SummonedWitherSkeleton;
import de.peoples_magic.entity.spells.SummonedZombie;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;

public class SummonBlazenKnightCommand {
    public SummonBlazenKnightCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("summon_blazen_knight")
                        .requires(source -> source.hasPermission(2))
                        .executes(
                                (context) -> ex(context.getSource().getPlayer())
                        ));
    }

    private int ex(ServerPlayer player){
        ServerLevel level = (ServerLevel) player.level();
        BlazenKnight z = ModEntities.BLAZEN_KNIGHT.get().create(level, EntitySpawnReason.COMMAND);
//        BlazenKnight z = new BlazenKnight(ModEntities.BLAZEN_KNIGHT.get(), level);
        z.setPos(player.position());
        level.addFreshEntity(z);
        return Command.SINGLE_SUCCESS;
    }
}
