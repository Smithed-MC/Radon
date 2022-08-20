# Radon
Radon is an experimental no-compromises drop-in fabric mod designed to apply targeted optimizations for minecraft commands-- especially in environments with many large datapacks. In testing, Radon reduced the mspt consumed by real datapacks by 20-40% depending on the situation. It accomplishes this by:
* Limiting serialization of NBT data to the requested data instead of all the data, saving massive time on data get (etc) operations. For example, if you do `data get entity @s Health` on a player, normally minecraft would copy & send *all* of the player NBT data, including the +1000 long array of unlocked recipes. With Radon, it will copy and send only Health and nothing more.
* Deserializing of NBT data is similaritly limited to requested data only. This is twice as effective since reading NBT data requires first writing nbt data, which uses the above optimization.
* Entities are cached based on their type and their selector tags. When using @e, the search function will check the caches for valid types (entity type tags are supported) and selector tags, and will perform the @e search only on the smallest cache. That is, if you @e[type=marker,tag=my_tag] and there are 100 markers and 50 my_tag, then the @e will only search the 50 entities with my_tag. Currently, this does not support type=! and tag=!.

## Setup

Put into your fabric mods folder or just download with the [smithed client](https://smithed.dev/).

