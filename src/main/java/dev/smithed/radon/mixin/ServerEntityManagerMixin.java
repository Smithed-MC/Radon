package dev.smithed.radon.mixin;

import dev.smithed.radon.mixin_interface.IEntityManagerExtender;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(ServerEntityManager.class)
public class ServerEntityManagerMixin<T extends EntityLike> implements IEntityManagerExtender<T> {

    @Shadow @Final private SectionedEntityCache<T> cache;
    private Map<String, EntityIndex<T>> taggedEntityIndexes = new HashMap<>();

    private Map<String, EntityLookup<T>> taggedEntityLookup = new HashMap<>();

    private void initTagLookup(String tag) {
        EntityIndex<T> index = taggedEntityIndexes.getOrDefault(tag, new EntityIndex<>());
        EntityLookup<T> lookup = taggedEntityLookup.getOrDefault(tag, new SimpleEntityLookup<>(index, this.cache));
        taggedEntityIndexes.put(tag, index);
        taggedEntityLookup.put(tag, lookup);
    }

    @Override
    public EntityLookup<T> getTaggedEntityLookup(String tag) {
        return taggedEntityLookup.get(tag);
    }

    @Inject(method = "addEntity(Lnet/minecraft/world/entity/EntityLike;Z)Z", at=@At("HEAD"))
    private void addEntityInject(T entity, boolean existing, CallbackInfoReturnable<Boolean> cir) {
        if(entity instanceof Entity le && !le.getScoreboardTags().isEmpty()) {
            le.getScoreboardTags().forEach((tag) -> {

                EntityLookup<T> entityLookup = taggedEntityLookup.getOrDefault(tag, new SimpleEntityLookup<T>(new EntityIndex<>(), this.cache));

                taggedEntityLookup.put(tag, entityLookup);
            });
        }
    }
}
