package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EyeOfEnderEntity.class)
public abstract class EyeOfEnderEntityMixin extends EntityMixin implements ICustomNBTMixin {
    @Shadow
    abstract ItemStack getTrackedItem();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        EyeOfEnderEntity entity = ((EyeOfEnderEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Item":
                    ItemStack itemStack = this.getTrackedItem();
                    if (!itemStack.isEmpty())
                        nbt.put("Item", itemStack.writeNbt(new NbtCompound()));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
