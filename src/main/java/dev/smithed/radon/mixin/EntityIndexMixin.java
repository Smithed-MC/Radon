package dev.smithed.radon.mixin;

import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IEntityIndexExtender;
import dev.smithed.radon.utils.NBTUtils;
import dev.smithed.radon.utils.SelectorContainer;
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

    @Shadow abstract <U extends T> void forEach(TypeFilter<T, U> filter, Consumer<U> action);
    private static final int REASONABLESEARCHSIZE = 100;
    private final Map<String, List<EntityLike>> entityMap = new HashMap<>();

    @Override
    public void addEntityToTagMap(String tag, EntityLike entity) {
        List<EntityLike> set = entityMap.computeIfAbsent(tag, k -> new ArrayList<>());
        set.add(entity);
    }

    @Override
    public void removeEntityFromTagMap(String tag, EntityLike entity) {
        List<EntityLike> set = entityMap.get(tag);
        if(set != null)
            set.removeAll(Collections.singleton(entity));
    }

    /**
     * @author ImCoolYeah105
     * Add entity type to map when loaded
     */
    @Inject(method = "add", at = @At("HEAD"))
    private void radon_addInject(T entity, CallbackInfo ci) {
        if(entity instanceof Entity ent) {
            String name = NBTUtils.translationToTypeName(ent.getType().getTranslationKey());
            if(name.length() > 0)
                this.addEntityToTagMap(name, entity);
        }
    }

    /**
     * @author ImCoolYeah105
     * Remove entity type & tags from map when unloaded
     */
    @Inject(method = "remove", at = @At("HEAD"))
    private void radon_removeInject(T entity, CallbackInfo ci) {
        if(entity instanceof Entity le && !le.getScoreboardTags().isEmpty()) {
            le.getScoreboardTags().forEach(tag -> removeEntityFromTagMap(tag, entity));
        }
        if(entity instanceof Entity ent) {
            String name = NBTUtils.translationToTypeName(ent.getType().getTranslationKey());
            if(name.length() > 0)
                this.removeEntityFromTagMap(name, entity);
        }
    }

    /**
     * This is a modified version of the forEach method in the base class.
     * It will check the cache for the type and tags of the selector, and use the smallest list of entities
     * retrieved for the @e search instead of all entities.
     */
    @Override
    public <U extends T> void forEachTaggedEntity(TypeFilter<T, U> filter, Consumer<U> action, SelectorContainer container) {
        List<EntityLike> set = null;
        List<List<EntityLike>> list = null;
        int size = Integer.MAX_VALUE;

        for (String tag : container.selectorTags) {
            List<EntityLike> result = this.entityMap.get(tag);
            if (result != null && result.size() < size) {
                set = result;
                size = result.size();

                if(size < REASONABLESEARCHSIZE)
                    break;
            }
        }

        if (size == 0)
            return;

        if(size >= REASONABLESEARCHSIZE) {
            if (!container.isNotType && !container.type.isBlank()) {
                if (container.isTypeTag) {
                    list = new LinkedList<>();
                    int mergeSize = 0;
                    for (String type : container.entityTypes) {
                        List<EntityLike> result = this.entityMap.get(type);
                        if (result != null) {
                            mergeSize += result.size();
                            list.add(result);
                        }
                    }
                    if (mergeSize < size) {
                        size = mergeSize;
                        set = null;
                    }
                } else {
                    List<EntityLike> result = this.entityMap.get(container.type);
                    if (result != null && result.size() < size) {
                        set = result;
                        size = result.size();
                    }
                }
            }
        }

        if (size == 0)
            return;

        if(Radon.CONFIG.debug)
            Radon.logDebug("searching on " + size + " entities for " + container);

        if (set != null) {
            set.forEach(entity -> {
                T entityLike = (T) entity;
                U entityLike2 = filter.downcast(entityLike);
                if (entityLike2 != null) {
                    action.accept(entityLike2);
                }
            });
        } else if (list != null) {
            list.forEach(iset -> iset.forEach(entity -> {
                T entityLike = (T) entity;
                U entityLike2 = filter.downcast(entityLike);
                if (entityLike2 != null) {
                    action.accept(entityLike2);
                }
            }));
        } else {
            this.forEach(filter, action);
        }
    }
}
