package dev.smithed.radon.mixin;

import dev.smithed.radon.mixin_interface.IEntityIndexExtender;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.entity.Entity;
import net.minecraft.util.TypeFilter;
import net.minecraft.world.entity.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.function.Consumer;

@Mixin(EntityIndex.class)
public abstract class EntityIndexMixin<T extends EntityLike> implements IEntityIndexExtender<T> {

    @Shadow @Final private Int2ObjectMap<T> idToEntity;

    @Shadow @Nullable public abstract T get(UUID uuid);

    private Map<String, List<UUID>> uuidMap = new HashMap<>();

    @Override
    public Map<String, List<UUID>> getUUIDMap() {
        return this.uuidMap;
    }
    public void addEntityToTagMap(String tag, UUID uuid) {
        List<UUID> uuids = uuidMap.get(tag);
        uuids.add(uuid);
        uuidMap.put(tag, uuids);
    }

    public void removeEntityFromTagMap(String tag, UUID uuid) {
        if(uuidMap.containsKey(tag)) {
            List<UUID> uuids = uuidMap.get(tag);
            uuids.remove(uuid);
            uuidMap.put(tag, uuids);
        }
    }

    @Inject(method = "add", at = @At("HEAD")) private void addInject(T entity, CallbackInfo ci) {
        if(entity instanceof Entity le && !le.getScoreboardTags().isEmpty()) {
            le.getScoreboardTags().forEach(tag -> addEntityToTagMap(tag, entity.getUuid()));
        }
    }

    @Inject(method = "remove", at = @At("HEAD")) private void removeInject(T entity, CallbackInfo ci) {
        if(entity instanceof Entity le && !le.getScoreboardTags().isEmpty()) {
            le.getScoreboardTags().forEach(tag -> removeEntityFromTagMap(tag, entity.getUuid()));
        }
    }

    public <U extends T> void forEachTaggedEntity(TypeFilter<T, U> filter, Consumer<U> action, String tag) {
        List<T> entities = new ArrayList<>();
        uuidMap.get(tag).forEach(uuid -> entities.add(get(uuid)));

        for (T t : entities) {
            U entityLike2 = filter.downcast(t);
            if (entityLike2 != null) {
                action.accept(entityLike2);
            }
        }

    }
}
