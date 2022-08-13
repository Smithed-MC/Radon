package dev.smithed.radon.mixin;

import com.google.common.collect.Lists;
import dev.smithed.radon.mixin_interface.IEntityManagerExtender;
import dev.smithed.radon.mixin_interface.IServerWorldExtender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.entity.EntityLookup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.function.Predicate;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements IServerWorldExtender {
    @Shadow @Final private ServerEntityManager<Entity> entityManager;

    @Override
    public <T extends Entity> List<T> getEntitiesByTag(TypeFilter<Entity, T> filter, Predicate<? super T> predicate, String tag) {
        List<T> list = Lists.newArrayList();
        if(this.entityManager instanceof IEntityManagerExtender extender) {
            extender.getTaggedEntityLookup(tag).forEach(
                    filter, (entity) -> {
                        if(predicate.test(entity)) {
                            list.add(entity);
                        }
                    }
            );
        }
        return list;
    }
}
