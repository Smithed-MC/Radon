package dev.smithed.radon.mixin;

import com.google.common.collect.Lists;
import dev.smithed.radon.mixin_interface.IEntityIndexExtender;
import dev.smithed.radon.mixin_interface.IEntityManagerExtender;
import dev.smithed.radon.mixin_interface.IServerWorldExtender;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.world.entity.EntityLike;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements IServerWorldExtender {
    @Shadow @Final private ServerEntityManager<Entity> entityManager;

    @Shadow public abstract <T extends Entity> List<? extends T> getEntitiesByType(TypeFilter<Entity, T> filter, Predicate<? super T> predicate);

    @Shadow @Nullable public abstract Entity getEntity(UUID uuid);

    @Override
    public <T extends Entity> List<? extends T> getEntitiesByTag(TypeFilter<Entity, T> filter, Predicate<? super T> predicate, String tag) {
        List<T> list = Lists.newArrayList();
        if(this.entityManager instanceof IEntityManagerExtender extender && extender.getEntityIndex() instanceof IEntityIndexExtender<?> iex && iex.getUUIDMap().containsKey(tag)) {
            iex.forEachTaggedEntity(filter, (entity) -> {
                if(predicate.test(entity)) {
                    list.add(entity);
                }
            }, tag);
        }
        else {
            list.addAll(getEntitiesByType(filter, predicate));
        }
        return list;
    }
}
