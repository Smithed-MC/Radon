package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CatEntity.class)
public abstract class CatEntityMixin extends TameableEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        CatEntity entity = ((CatEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "variant":
                    nbt.putString("variant", Registry.CAT_VARIANT.getId(entity.getVariant()).toString());
                    break;
                case "CollarColor":
                    nbt.putByte("CollarColor", (byte)entity.getCollarColor().getId());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
