package dev.smithed.radon.mixin_interface;

import dev.smithed.radon.utils.SelectorContainer;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.world.entity.EntityIndex;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public interface IServerWorldExtender {

    EntityIndex<?> getEntityIndex();

    <T extends Entity> void collectEntitiesByType(TypeFilter<Entity, T> filter, Predicate<? super T> predicate, List<? super T> result, int limit, SelectorContainer container);

}
