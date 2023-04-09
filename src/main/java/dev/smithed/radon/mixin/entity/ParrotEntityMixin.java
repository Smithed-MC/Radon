package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ParrotEntity.class)
public abstract class ParrotEntityMixin extends TameableEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ParrotEntity entity = ((ParrotEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            //TODO: access private id field
            //case "Variant":
            //    nbt.putInt("Variant", entity.getVariant().id);
            //    break;
            return false;
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ParrotEntity entity = ((ParrotEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            if (topLevelNbt.equals("Variant")) {
                entity.setVariant(ParrotEntity.Variant.byIndex(nbt.getInt("Variant")));
            } else {
                return false;
            }
        }
        return true;
    }
}
