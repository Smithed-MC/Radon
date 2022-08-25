# Radon
Radon is an experimental no-compromises drop-in fabric mod designed to apply targeted optimizations for minecraft commands-- especially in environments with many large datapacks. In testing, Radon reduced the mspt consumed by several large, well developed datapacks* by ~50% depending on the situation (datapacks using a lot of @e or nbt access could see even better performance gains). It accomplishes this by:
* Limiting serialization of NBT data to the requested data instead of all the data, saving massive time on data get (etc) operations. For example, if you do `data get entity @s Health` on a player, normally minecraft would copy & send *all* of the player NBT data, including the +1000 long array of unlocked recipes. With Radon, it will copy and send only Health and nothing more.
* Deserializing of NBT data is similaritly limited to requested data only. This is twice as effective since reading NBT data requires first writing nbt data, which uses the above optimization.
* Entities are cached based on their type and their selector tags. When using @e, the search function will check the caches for valid types (entity type tags are supported) and selector tags, and will perform the @e search only on the smallest cache. That is, if you @e[type=marker,tag=my_tag] and there are 100 markers and 50 my_tag, then the @e will only search the 50 entities with my_tag. Currently, this does not support type=! and tag=!.
* Radon fixes [MC-168596](https://bugs.mojang.com/browse/MC-168596) "Chunks outside of render distance are not unloaded if 'execute if block' runs on it every tick." While this may not be noticible in single player, on a server hundreds of chunks can be forceloaded in random player's bases do to custom blocks receiving tickly if block checks.

Testing included a server with these datapacks combined: Mechanization, The Creeper's Code, Florcraft, Dinos and Dodos, Ocean Additions, Terralith, Manic, Sanguine, Grappling Hooks, Explorer's Advent, and Call of Chaos.

## Setup

Download the latest version from [Releases](https://github.com/Smithed-MC/Radon/releases) and place the mod in your [Fabric](https://fabricmc.net/) mods folder.

(WIP) Alternativly, use the [smithed client](https://smithed.dev/), a new Minecraft launcher which includes datapack searching, automatic merging, and some performance mod options.

## Advanced Features
Want to see the difference for yourself? Radon supports toggling optimizations on the fly using these commands. You can observe the mspt in f3, or run a profile with f3+L in single player and /perf <start/stop> in multiplayer:
* /radon nbt-optimiations <true/false>
* /radon selector-optimizations <true/false>
* /radon fix-block-access-forceload <true/false>

Radon also has a debug mode, which can print potential problems to console if you find your commands aren't running faster or worse are running slower. Don't toggle this on while running commands tickly or you'll get console spam:
* /radon debug-mode <true/false>
