package dev.smithed.radon.mixin;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IEntityIndexExtender;
import dev.smithed.radon.utils.NBTUtils;
import dev.smithed.radon.utils.SelectorContainer;
import net.minecraft.entity.Entity;
import net.minecraft.util.TypeFilter;
import net.minecraft.world.entity.*;
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

    @Shadow
    public abstract T get(UUID uuid);
    @Final
    @Shadow
    private Map<UUID, T> uuidToEntity;
    @Shadow
    abstract <U extends T> void forEach(TypeFilter<T, U> filter, Consumer<U> action);

    private final Map<String, Set<EntityLike>> entityMap = new HashMap<>();

    @Override
    public void addEntityToTagMap(String tag, EntityLike entity) {
        Set<EntityLike> set = entityMap.computeIfAbsent(tag, k -> new HashSet<>());
        set.add(entity);
    }

    @Override
    public void removeEntityFromTagMap(String tag, EntityLike entity) {
        Set<EntityLike> set = entityMap.get(tag);
        if(set != null)
            set.remove(entity);
    }

    @Inject(method = "add", at = @At("HEAD"))
    private void addInject(T entity, CallbackInfo ci) {
        if(entity instanceof Entity ent) {
            String name = NBTUtils.translationToTypeName(ent.getType().getTranslationKey());
            if(name.length() > 0)
                this.addEntityToTagMap(name, entity);
        }
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void removeInject(T entity, CallbackInfo ci) {
        if(entity instanceof Entity le && !le.getScoreboardTags().isEmpty()) {
            le.getScoreboardTags().forEach(tag -> removeEntityFromTagMap(tag, entity));
        }
        if(entity instanceof Entity ent) {
            String name = NBTUtils.translationToTypeName(ent.getType().getTranslationKey());
            if(name.length() > 0)
                this.removeEntityFromTagMap(name, entity);
        }
    }

    @Override
    public <U extends T> void forEachTaggedEntity(TypeFilter<T, U> filter, Consumer<U> action, SelectorContainer container) {
        Set<EntityLike> set = null;

        if(container.type.length() > 0 && !container.isNotType) {
            if (container.isTypeTag) {
                set = new HashSet<>();
                for(String type: container.entityTypes) {
                    Set<EntityLike> entries = this.entityMap.get(type);
                    if(entries != null)
                        set = Sets.union(set, entries);
                }
            } else {
                set = this.entityMap.get(container.type);
            }

            if(set == null || set.size() == 0)
                return;
        }

        for(String tag: container.selectorTags) {
            Set<EntityLike> interSet = this.entityMap.get(tag);
            if(interSet == null)
                return;
            if(set == null)
                set = this.entityMap.get(tag);
            else
                set = Sets.intersection(set, interSet);
        }

        if(set == null) {
            this.forEach(filter, action);
            return;
        }

        for(String tag: container.notSelectorTags) {
            Set<EntityLike> interSet = this.entityMap.get(tag);
            if(interSet != null)
                set = Sets.difference(set, interSet);
        }

        set.forEach(entity -> {
            T entityLike = (T) entity;
            U entityLike2 = filter.downcast(entityLike);
            if (entityLike2 != null) {
                action.accept(entityLike2);
            }
        });
    }
}
