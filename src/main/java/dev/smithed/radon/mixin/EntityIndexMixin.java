package dev.smithed.radon.mixin;

import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IEntityIndexExtender;
import net.minecraft.entity.Entity;
import net.minecraft.util.TypeFilter;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.function.Consumer;

@Mixin(EntityIndex.class)
public abstract class EntityIndexMixin<T extends EntityLike> implements IEntityIndexExtender<T> {

    @Shadow
    public abstract T get(UUID uuid);

    private Map<String, List<UUID>> uuidMap = new HashMap<>();

    @Override
    public void addEntityToTagMap(String tag, UUID uuid) {
        if(!uuidMap.containsKey(tag))
            uuidMap.put(tag, new LinkedList<>());
        uuidMap.get(tag).add(uuid);
    }

    @Override
    public void removeEntityFromTagMap(String tag, UUID uuid) {
        List<UUID> list = uuidMap.get(tag);
        if(list != null) {
            list.remove(uuid);
            if(list.size() == 0)
                uuidMap.remove(tag);
        }
    }

    @Inject(method = "add", at = @At("HEAD"))
    private void addInject(T entity, CallbackInfo ci) {
        if(entity instanceof Entity le && !le.getScoreboardTags().isEmpty()) {
            le.getScoreboardTags().forEach(tag -> addEntityToTagMap(tag, entity.getUuid()));
        }
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void removeInject(T entity, CallbackInfo ci) {
        if(entity instanceof Entity le && !le.getScoreboardTags().isEmpty()) {
            le.getScoreboardTags().forEach(tag -> removeEntityFromTagMap(tag, entity.getUuid()));
        }
    }

    @Override
    public <U extends T> void forEachTaggedEntity(TypeFilter<T, U> filter, Consumer<U> action, String tag) {
        List<UUID> list = uuidMap.get(tag);
        if(list != null) {
            Radon.logDebug("@e tag size = " + list.size());
            for(UUID uuid: list) {
                T entityLike = this.get(uuid);
                U entityLike2 = filter.downcast(entityLike);
                if (entityLike2 != null) {
                    action.accept(entityLike2);
                }
            };
        }
    }
}
