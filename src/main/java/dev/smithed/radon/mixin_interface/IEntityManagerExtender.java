package dev.smithed.radon.mixin_interface;

import net.minecraft.entity.Entity;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityLookup;

public interface IEntityManagerExtender<T extends EntityLike> {

    EntityLookup<T> getTaggedEntityLookup(String tag);

}
