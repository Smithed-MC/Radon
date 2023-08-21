package dev.smithed.radon.mixin;

import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IEntityIndexExtender;
import dev.smithed.radon.utils.NBTUtils;
import dev.smithed.radon.utils.SelectorContainer;
import net.minecraft.entity.Entity;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.function.LazyIterationConsumer;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(EntityIndex.class)
public abstract class EntityIndexMixin<T extends EntityLike> implements IEntityIndexExtender<T> {

    @Shadow abstract <U extends T> void forEach(TypeFilter<T, U> filter, LazyIterationConsumer<U> consumer);
    private static final int REASONABLESEARCHSIZE = 100;
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
            set.removeAll(Collections.singleton(entity));
    }

    /**
     * @author ImCoolYeah105
     * Add entity type to map when loaded
     */
    @Inject(method = "add", at = @At("HEAD"))
    private void radon_add(T entityLike, CallbackInfo ci) {
        if(entityLike instanceof Entity entity) {
            String name = NBTUtils.translationToTypeName(entity.getType().getTranslationKey());
            if(name.length() > 0)
                this.addEntityToTagMap(name, entityLike);
            if(!entity.getCommandTags().isEmpty())
                entity.getCommandTags().forEach(tag -> addEntityToTagMap(tag, entityLike));
        }
    }

    /**
     * @author ImCoolYeah105
     * Remove entity type & tags from map when unloaded
     */
    @Inject(method = "remove", at = @At("HEAD"))
    private void radon_remove(T entityLike, CallbackInfo ci) {
        if(entityLike instanceof Entity entity) {
            String name = NBTUtils.translationToTypeName(entity.getType().getTranslationKey());
            if(name.length() > 0)
                this.removeEntityFromTagMap(name, entity);
            if(!entity.getCommandTags().isEmpty())
                entity.getCommandTags().forEach(tag -> removeEntityFromTagMap(tag, entityLike));
        }
    }

    /**
     * This is a modified version of the forEach method in the base class.
     * It will check the cache for the type and tags of the selector, and use the smallest list of entities
     * retrieved for the @e search instead of all entities.
     */
    @Override
    public <U extends T> void forEachTaggedEntity(TypeFilter<T, U> filter, SelectorContainer container, LazyIterationConsumer<U> action) {
        Set<EntityLike> set = null;
        List<Set<EntityLike>> list = null;
        int size = Integer.MAX_VALUE;

        for (String tag : container.selectorTags) {
            Set<EntityLike> result = this.entityMap.get(tag);
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
                        Set<EntityLike> result = this.entityMap.get(type);
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
                    Set<EntityLike> result = this.entityMap.get(container.type);
                    if (result != null && result.size() < size) {
                        set = result;
                        size = result.size();
                    }
                }
            }
        }

        if (size == 0)
            return;

        Radon.logDebugFormat("searching on %s entities for %s", size, container);

        if (set != null) {
            forEachInCollection(set, filter, action);
        } else if (list != null) {
            list.forEach(iset -> forEachInCollection(iset, filter, action));
        } else {
            this.forEach(filter, action);
        }
    }

    public <U extends T> void forEachInCollection(Collection<EntityLike> collection, TypeFilter<T, U> filter, LazyIterationConsumer<U> consumer) {
        Iterator<EntityLike> iterator = collection.iterator();

        U entityLike2;
        do {
            if (!iterator.hasNext()) {
                return;
            }
            T entityLike = (T)iterator.next();
            entityLike2 = filter.downcast(entityLike);
        } while(entityLike2 == null || !consumer.accept(entityLike2).shouldAbort());

    }
}
