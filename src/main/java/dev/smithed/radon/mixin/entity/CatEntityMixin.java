package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
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

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        CatEntity entity = ((CatEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "variant":
                    CatVariant catVariant = Registry.CAT_VARIANT.get(Identifier.tryParse(nbt.getString("variant")));
                    if (catVariant != null)
                        entity.setVariant(catVariant);
                    break;
                case "CollarColor":
                    if (nbt.contains("CollarColor", 99))
                        entity.setCollarColor(DyeColor.byId(nbt.getInt("CollarColor")));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
