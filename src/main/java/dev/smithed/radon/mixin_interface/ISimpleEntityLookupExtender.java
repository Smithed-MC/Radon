package dev.smithed.radon.mixin_interface;

import dev.smithed.radon.utils.SelectorContainer;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.function.LazyIterationConsumer;
import net.minecraft.world.entity.EntityIndex;
import net.minecraft.world.entity.EntityLike;

import java.util.Set;
import java.util.function.Consumer;

public interface ISimpleEntityLookupExtender<T extends EntityLike> {

    EntityIndex getIndex();
    <U extends T> void forEachTaggedEntity(TypeFilter<T, U> filter, LazyIterationConsumer<U> action, SelectorContainer container);

}
