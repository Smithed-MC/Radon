package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ThrownItemEntity.class)
public abstract class ThrownItemEntityMixin extends ProjectileEntityMixin implements ICustomNBTMixin {
    @Shadow
    protected abstract ItemStack getItem();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ThrownItemEntity entity = ((ThrownItemEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Item":
                    ItemStack itemStack = this.getItem();
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
