# Radon
Radon is an experimental no-compromises drop-in fabric mod designed to apply targeted optimizations for minecraft commands-- especially in environments with many datapacks. It currently:
* Limits serialization of NBT data to the requested data instead of all the data, saving massive time on serialization (especially for players)
* Selector tags are cached so tag= only searches entities that are known to already have the tag. Currently, if multiple tag= are used, the rightmost tag= is used to access the cache. Therefor, this should be your most restrictive tag.

## Setup

Put into your fabric mods folder or just download with the [smithed client](https://smithed.dev/).

