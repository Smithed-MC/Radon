# Radon
Radon is an experimental no-compromises drop-in Fabric mod designed to apply targeted optimizations for Minecraft commands-- especially in environments with many large datapacks. In testing, Radon reduced the mspt consumed by several large, well-developed datapacks* by ~50% depending on the situation (datapacks using a lot of @e or NBT access could see even better performance gains). It accomplishes this by:
* Limiting serialization of NBT data to the requested data instead of all the data, saving massive time on `data get` (etc) operations. For example, if you do `data get entity @s Health` on a player, normally Minecraft would copy & send *all* of the player NBT data, including the +1000 long array of unlocked recipes. With Radon, it will copy and send only `Health` and nothing more.
* Deserializing of NBT data is similarly limited to requested data only. This is twice as effective since reading NBT data requires first writing nbt data, which uses the above optimization.
* Entities are cached based on their type and their selector tags. When using @e, the search function will check the caches for valid types (entity type tags are supported) and selector tags, and will perform the @e search only on the smallest cache. That is, if you select `@e[type=marker,tag=my_tag]` and there are 100 markers, 50 with `my_tag`, then the @e will only search the 50 entities with `my_tag`. Currently, this does not support type=! and tag=!. Note: As of 1.9.3, Minecraft limits @e searches to specific sub-chunks when using distance= and dx,dy,dz=. Radon makes no attempt to optimize these searches, it only does global searches.
* Radon fixes [MC-168596](https://bugs.mojang.com/browse/MC-168596) "Chunks outside of render distance are not unloaded if 'execute if block' runs on it every tick." While this may not be noticeable in single player, on a server hundreds of chunks can be force-loaded in random players' bases due to custom blocks receiving tickly `if block` checks.

*Testing included a server with these datapacks combined: [Mechanization](https://github.com/ICY105/Mechanization), [The Creeper's Code](https://github.com/CreeperMagnet/the-creepers-code/), [Florcraft](https://github.com/eatYourHashs/florcraft), [Dinos and Dodos](https://github.com/RagtimeGal/DnD), [Ocean Additions](https://github.com/primalugly/Ocean-Additions), [Terralith](https://www.curseforge.com/minecraft/mc-mods/terralith), [Manic](https://github.com/VisiVersa/Manic), [Sanguine](https://github.com/VisiVersa/Sanguine), [Grappling Hooks](https://github.com/asdru22/packs/tree/main/grappling%20hook), [Explorer's Advent](https://github.com/Jachr0/epa_public), and [Call of Chaos](https://github.com/TheNuclearNexus/CallOfChaos).

## Proof
Worst-case scenario for player NBT lookup: `data get entity @s Health` 10000 times per tick on a player with a full inventory, ender chest, and all recipes unlocked:

![Player NBT](https://cdn.discordapp.com/attachments/507995770109165579/1012754969088249926/player_nbt.png)

Running `execute if entity @e[type=minecraft:item_frame]` 10000 times per tick while 5120 markers are loaded and 320 item frames are loaded:

![selector](https://cdn.discordapp.com/attachments/507995770109165579/1012754969478307890/selector.png)

## Usage
Radon is experimental, and you should not develop a datapack with the expectation its users will be using Radon. After all, the whole point of a datapack is that it works in vanilla. If you expect users to use a mod, just make a mod.

That said, if you're setting up a server with a lot of datapacks that's already using performance mods (ie. Lithium), or your computer is truly a potato and you need to squeeze out every drop of performance you can get, then definitely give Radon a try.

### Setup

Download the latest version from [Releases](https://github.com/Smithed-MC/Radon/releases) and place the mod in your [Fabric](https://fabricmc.net/) mods folder.

(WIP) Alternativly, use the [Smithed client](https://smithed.net/), a new Minecraft launcher which includes datapack searching, automatic merging, and some performance mod options.

## Advanced Features
Want to see the difference for yourself? Radon supports toggling optimizations on the fly using these commands. You can observe the mspt in f3, or run a profile with f3+L in single player and /perf <start/stop> in multiplayer:
* `/radon nbt-optimiations <true/false>`
* `/radon selector-optimizations <true/false>`
* `/radon fix-block-access-forceload <true/false>`

Radon also has a debug mode, which can print potential problems to console if you find your commands aren't running faster or worse are running slower. Don't toggle this on while running commands tickly or you'll get console spam:
* `/radon debug-mode <true/false>`
