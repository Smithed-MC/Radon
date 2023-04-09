package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TropicalFishEntity.class)
public abstract class TropicalFishEntityMixin extends FishEntityMixin implements ICustomNBTMixin {

    @Shadow abstract int getTropicalFishVariant();
    @Shadow abstract void setTropicalFishVariant(int variant);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TropicalFishEntity entity = ((TropicalFishEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("Variant")) {
                nbt.putInt("Variant", this.getTropicalFishVariant());
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TropicalFishEntity entity = ((TropicalFishEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            if (topLevelNbt.equals("Variant")) {
                this.setTropicalFishVariant(nbt.getInt("Variant"));
            } else {
                return false;
            }
        }
        return true;
    }
}
