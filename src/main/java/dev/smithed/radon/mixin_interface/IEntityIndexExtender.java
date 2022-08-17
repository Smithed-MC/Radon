package dev.smithed.radon.mixin_interface;

import dev.smithed.radon.utils.SelectorContainer;
import net.minecraft.util.TypeFilter;
import net.minecraft.world.entity.EntityLike;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public interface IEntityIndexExtender<T extends EntityLike> {

    void addEntityToTagMap(String tag, EntityLike entity);

    void removeEntityFromTagMap(String tag, EntityLike entity);

    <U extends T> void forEachTaggedEntity(TypeFilter<T, U> filter, Consumer<U> action, SelectorContainer container);

}
