package de.peoples_magic.entity.spells;

import net.minecraft.world.entity.player.Player;

public interface SummonedEntity {

    Player get_owner();
    void set_owner(Player p);

    int get_ticks_to_live();
    void set_ticks_to_live(int t);
}
