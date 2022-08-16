package dev.smithed.radon.mixin;

import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IEntitySelectorExtender;
import dev.smithed.radon.mixin_interface.IEntitySelectorReaderExtender;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedHashSet;
import java.util.Set;

@Mixin(EntitySelectorReader.class)
public class EntitySelectorReaderMixin implements IEntitySelectorReaderExtender {

    private Set<String> tags = new LinkedHashSet<>();

    @Override
    public Set<String> getReaderTags() {
        return tags;
    }

    @Override
    public void addReaderTag(String tag) {
        this.tags.add(tag);
    }

    @Inject(method = "build", at = @At("RETURN"), cancellable = true)
    private void BuildInject(CallbackInfoReturnable<EntitySelector> cir) {
        if(cir.getReturnValue() instanceof IEntitySelectorExtender extender) {
            extender.setTags(this.tags);
            cir.setReturnValue((EntitySelector) extender);
        }
    }
}
