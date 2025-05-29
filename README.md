# Source Code for the Peoples Magic Neoforge Mod

Don't use this code for reference much, I am new to this :D

# Documentation

## Configuration
Many aspects about the mod can be configured in the ``peoples_magic-common.toml`` config file.
This includes:
- mana cost and other aspects of spells
- the progression threshold for each spell
- amount of life and damage values for all bosses
- values for natural mana regeneration, mana wells and mana enchantments

## Admin Commands
These commands require you to have admin privileges in the world:
### Spell progression
- ``/set_absorption_mitigation <player> <amount>`` sets the amount of half-hearts the player has mitigated
- ``/set_aether_grip_pulls <player> <amount>`` sets the amount of skeletons the player has pulled
- ``/set_farming_breeds <player> <amount>`` sets the amount of bred animals for a player
- ``/set_fireball_dmg <player> <amount>`` sets the amount of direct fireball damage for a player
- ``/set_haste_uptime <player> <amount>`` sets the amount of haste uptime for a player. The time is measured in ticks, so every second of uptime amounts to 20 ticks
- ``/set_ice_cone_multiple_hits <player> <amount>`` sets the amount of multi-hits for a player
- ``/set_repel_spiders_repelled <player> <amount>`` sets the amount of spiders repelled mid air for a player
- ``/set_summons_count <player> <amount>`` sets the amount of allies summoned for a player

### Testing
- ``/summon_ally`` summons the current ally for your "Summon Ally" level
- ``/summon_blazen_knight`` summons a "Blazen Knight" boss
- ``/summon_forest_guardian`` summons a "Guardian of the Forest" boss
- ``/summon_sky_scourge`` summons a "Scourge of the Sky" boss