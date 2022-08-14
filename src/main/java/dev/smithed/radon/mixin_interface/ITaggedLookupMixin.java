package dev.smithed.radon.mixin_interface;

import net.minecraft.util.TypeFilter;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityLookup;

import java.util.function.Consumer;

public interface ITaggedLookupMixin <T extends EntityLike> extends EntityLookup<T> {

    <U extends T> void forEachTaggedEntity(TypeFilter<T, U> filter, Consumer<U> action, String tag);

}
