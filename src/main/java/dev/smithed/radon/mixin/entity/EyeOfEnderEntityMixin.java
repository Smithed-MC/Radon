package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EyeOfEnderEntity.class)
public abstract class EyeOfEnderEntityMixin extends EntityMixin implements ICustomNBTMixin {
    @Shadow abstract ItemStack getTrackedItem();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        EyeOfEnderEntity entity = ((EyeOfEnderEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("Item")) {
                ItemStack itemStack = this.getTrackedItem();
                if (!itemStack.isEmpty())
                    nbt.put("Item", itemStack.writeNbt(new NbtCompound()));
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        EyeOfEnderEntity entity = ((EyeOfEnderEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            if (topLevelNbt.equals("Item")) {
                ItemStack itemStack = ItemStack.fromNbt(nbt.getCompound("Item"));
                entity.setItem(itemStack);
            } else {
                return false;
            }
        }
        return true;
    }
}
