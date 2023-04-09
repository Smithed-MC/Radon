package dev.smithed.radon.mixin;

import dev.smithed.radon.mixin_interface.IEntityIndexExtender;
import dev.smithed.radon.mixin_interface.IServerWorldExtender;
import dev.smithed.radon.mixin_interface.ISimpleEntityLookupExtender;
import dev.smithed.radon.utils.SelectorContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.function.LazyIterationConsumer;
import net.minecraft.util.math.Box;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.entity.EntityIndex;
import net.minecraft.world.entity.EntityLookup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements IServerWorldExtender {

    @Shadow @Final MinecraftServer server;
    @Shadow abstract <T extends Entity> List<? extends T> getEntitiesByType(TypeFilter<Entity, T> filter, Predicate<? super T> predicate);
    @Shadow abstract EntityLookup<Entity> getEntityLookup();
    @Shadow abstract Profiler getProfiler();

    @Override
    public EntityIndex<Entity> getEntityIndex() {
        if(this.getEntityLookup() instanceof ISimpleEntityLookupExtender<?> lookup)
            return lookup.getIndex();
        else
            return null;
    }

    @Override
    public <T extends Entity> void collectEntitiesByType(TypeFilter<Entity, T> filter, Box box, Predicate<? super T> predicate, List<? super T> result, int limit, SelectorContainer container) {
        this.getProfiler().visit("getEntities");
        this.getEntityLookup().forEachIntersects(filter, box, (entity) -> {
            if (predicate.test(entity)) {
                result.add(entity);
                if (result.size() >= limit) {
                    return LazyIterationConsumer.NextIteration.ABORT;
                }
            }

            if (entity instanceof EnderDragonEntity enderDragonEntity) {
                EnderDragonPart[] var6 = enderDragonEntity.getBodyParts();

                for (EnderDragonPart enderDragonPart : var6) {
                    T entity2 = filter.downcast(enderDragonPart);
                    if (entity2 != null && predicate.test(entity2)) {
                        result.add(entity2);
                        if (result.size() >= limit) {
                            return LazyIterationConsumer.NextIteration.ABORT;
                        }
                    }
                }
            }

            return LazyIterationConsumer.NextIteration.CONTINUE;
        });
    }

    @Override
    public <T extends Entity> void collectEntitiesByType(TypeFilter<Entity, T> filter, Predicate<? super T> predicate, List<? super T> result, int limit, SelectorContainer container) {
        IEntityIndexExtender<Entity> extender = getEntityLookupExtender();
        if (extender != null) {
            extender.forEachTaggedEntity(filter, container, (entity) -> {
                if (predicate.test(entity)) {
                    result.add(entity);
                    if (result.size() >= limit) {
                        return LazyIterationConsumer.NextIteration.ABORT;
                    }
                }

                return LazyIterationConsumer.NextIteration.CONTINUE;
            });
        } else {
            ((ServerWorld) (Object) this).collectEntitiesByType(filter, predicate, result, limit);
        }
    }

    private IEntityIndexExtender<Entity> getEntityLookupExtender() {
        EntityIndex<Entity> index = this.getEntityIndex();
        if(index instanceof IEntityIndexExtender<?> mixin)
            return (IEntityIndexExtender<Entity>) mixin;
        return null;
    }

}
