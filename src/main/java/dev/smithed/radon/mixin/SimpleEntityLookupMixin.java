package dev.smithed.radon.mixin;

import dev.smithed.radon.mixin_interface.IEntityIndexExtender;
import dev.smithed.radon.mixin_interface.ISimpleEntityLookupExtender;
import dev.smithed.radon.utils.SelectorContainer;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.function.LazyIterationConsumer;
import net.minecraft.world.entity.EntityIndex;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.SimpleEntityLookup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(SimpleEntityLookup.class)
public abstract class SimpleEntityLookupMixin<T extends EntityLike> implements ISimpleEntityLookupExtender<T> {
    @Shadow @Final EntityIndex<T> index;

    @Override
    public <U extends T> void forEachTaggedEntity(TypeFilter<T, U> filter, LazyIterationConsumer<U> action, SelectorContainer container) {
        if(this.index instanceof IEntityIndexExtender tagged)
            tagged.forEachTaggedEntity(filter, container, action);
        else
            this.index.forEach(filter, action);
    }

    public EntityIndex<T> getIndex() {
        return this.index;
    }

}
