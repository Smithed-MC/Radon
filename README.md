# Radon
Radon is an experimental no-compromises drop-in fabric mod designed to apply targeted optimizations for minecraft commands-- especially in environments with many datapacks. It currently:
* Limits serialization of NBT data to the requested data instead of all the data, saving massive time on serialization (especially for players)
* Entities are cached based on their type and thier selector tags. When using @e, the search function will check the caches for valid types (entity type tags are supported) and tags and use the smallest cache to search for. That is, if you @e[type=marker,tag=my_tag] and there are 100 markers and 50 my_tag, then the @e will only search the 50 entities with my_tag. Currently, this does not support type=! and tag=!.

## Setup

Put into your fabric mods folder or just download with the [smithed client](https://smithed.dev/).

