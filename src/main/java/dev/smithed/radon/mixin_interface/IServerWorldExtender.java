package dev.smithed.radon.mixin_interface;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.function.Predicate;

public interface IServerWorldExtender {

    public <T extends Entity> List<? extends T> getEntitiesByTag(TypeFilter<Entity, T> filter, Predicate<? super T> predicate, String tag);

}
