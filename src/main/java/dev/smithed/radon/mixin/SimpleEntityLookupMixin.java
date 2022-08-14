package dev.smithed.radon.mixin;

import dev.smithed.radon.mixin_interface.ISimpleEntityLookupExtender;
import dev.smithed.radon.mixin_interface.ITaggedLookupMixin;
import net.minecraft.util.TypeFilter;
import net.minecraft.world.entity.EntityIndex;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.SimpleEntityLookup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(SimpleEntityLookup.class)
public abstract class SimpleEntityLookupMixin<T extends EntityLike> implements ISimpleEntityLookupExtender, ITaggedLookupMixin<T> {
    @Final
    @Shadow
    private EntityIndex<T> index;

    @Override
    public <U extends T> void forEachTaggedEntity(TypeFilter<T, U> filter, Consumer<U> action, String tag) {
        if(this.index instanceof ITaggedLookupMixin tagged)
            tagged.forEachTaggedEntity(filter, action, tag);
        else
            this.index.forEach(filter, action);
    }

    public EntityIndex<T> getIndex() {
        return this.index;
    }

}
