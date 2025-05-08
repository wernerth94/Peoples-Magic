package de.peoples_magic.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.peoples_magic.SpellUtil;
import de.peoples_magic.entity.spells.SummonedSkeleton;
import de.peoples_magic.entity.spells.SummonedWitherSkeleton;
import de.peoples_magic.entity.spells.SummonedZombie;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;

public class SummonAllyCommand {
    public SummonAllyCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("summon_ally")
                        .requires(source -> source.hasPermission(2))
                        .executes(
                                (context) -> ex(context.getSource().getPlayer())
                        ));
    }

    private int ex(ServerPlayer player){
        ServerLevel level = (ServerLevel) player.level();
        int spell_level = SpellUtil.get_summon_ally_level(player);
        switch (spell_level) {
            case 0:
            case 1:
            case 2:
                SummonedZombie z = new SummonedZombie(EntityType.ZOMBIE, level, spell_level);
                z.set_owner(player);
                z.set_ticks_to_live(10*20);
                z.setPos(player.getPosition(0));
                level.addFreshEntity(z);
                break;
            case 3:
            case 4:
            case 5:
                SummonedSkeleton skelly = new SummonedSkeleton(EntityType.SKELETON, level, spell_level);
                skelly.set_owner(player);
                skelly.set_ticks_to_live(10*20);
                skelly.setPos(player.getPosition(0));
                level.addFreshEntity(skelly);
                break;
            case 6:
            default:
                SummonedWitherSkeleton wither_skelly = new SummonedWitherSkeleton(EntityType.WITHER_SKELETON, level);
                wither_skelly.set_owner(player);
                wither_skelly.set_ticks_to_live(10*20);
                wither_skelly.setPos(player.getPosition(0));
                level.addFreshEntity(wither_skelly);
                break;
        }
        return Command.SINGLE_SUCCESS;
    }
}
