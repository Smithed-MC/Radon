package dev.smithed.radon.mixin;

import com.google.common.collect.Sets;
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

    private final Map<String, Set<UUID>> uuidMap = new HashMap<>();

    @Override
    public void addEntityToTagMap(String tag, UUID uuid) {
        Set<UUID> set = uuidMap.computeIfAbsent(tag, k -> new HashSet<>());
        set.add(uuid);
    }

    @Override
    public void removeEntityFromTagMap(String tag, UUID uuid) {
        Set<UUID> set = uuidMap.get(tag);
        if(set != null)
            set.remove(uuid);
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void removeInject(T entity, CallbackInfo ci) {
        if(entity instanceof Entity le && !le.getScoreboardTags().isEmpty()) {
            le.getScoreboardTags().forEach(tag -> removeEntityFromTagMap(tag, entity.getUuid()));
        }
    }

    @Override
    public <U extends T> void forEachTaggedEntity(TypeFilter<T, U> filter, Consumer<U> action, Set<String> tags) {
        Set<UUID> set = null;
        for(String tag: tags) {
            Set<UUID> interSet = this.uuidMap.get(tag);
            if(interSet == null)
                return;
            if(set == null)
                set = this.uuidMap.get(tag);
            else
                set = Sets.intersection(set, interSet);
        }

        if(set != null) {
            if(Radon.CONFIG.debug) {
                StringBuilder tagNames = new StringBuilder();
                for (String tag : tags) {
                    tagNames.append(tag).append(", ");
                }
                Radon.logDebug("@e tag = " + tagNames + " size = " + set.size());
            }
            set.forEach(uuid -> {
                T entityLike = this.get(uuid);
                U entityLike2 = filter.downcast(entityLike);
                if (entityLike2 != null) {
                    action.accept(entityLike2);
                }
            });
        }
    }
}
