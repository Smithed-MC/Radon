package dev.smithed.radon.mixin_interface;

import dev.smithed.radon.utils.SelectorContainer;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.function.LazyIterationConsumer;
import net.minecraft.world.entity.EntityLike;

import java.util.Map;

public interface IEntityIndexExtender<T extends EntityLike> {

    void addEntityToTagMap(String tag, EntityLike entity);

    void removeEntityFromTagMap(String tag, EntityLike entity);

    <U extends T> void forEachTaggedEntity(TypeFilter<T, U> filter, SelectorContainer container, LazyIterationConsumer<U> action);

}
