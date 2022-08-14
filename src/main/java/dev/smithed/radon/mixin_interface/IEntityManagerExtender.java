package dev.smithed.radon.mixin_interface;


import net.minecraft.world.entity.EntityIndex;
import net.minecraft.world.entity.EntityLike;

public interface IEntityManagerExtender<T extends EntityLike> {

    EntityIndex<T> getEntityIndex();

}
