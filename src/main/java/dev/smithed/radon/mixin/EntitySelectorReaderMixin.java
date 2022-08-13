package dev.smithed.radon.mixin;

import dev.smithed.radon.mixin_interface.IEntitySelectorExtender;
import dev.smithed.radon.mixin_interface.IEntitySelectorReaderExtender;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntitySelectorReader.class)
public class EntitySelectorReaderMixin implements IEntitySelectorReaderExtender {

    private String ReaderTag;

    @Override
    public String getReaderTag() {
        return ReaderTag;
    }

    @Override
    public void setReaderTag(String tag) {
        this.ReaderTag = tag;
    }

    @Inject(method = "build", at = @At("RETURN"))
    private void BuildInject(CallbackInfoReturnable<EntitySelector> cir) {
        if(cir.getReturnValue() instanceof IEntitySelectorExtender extender) {
            extender.setTag(getReaderTag());
            cir.setReturnValue((EntitySelector) extender);
        }
    }
}
