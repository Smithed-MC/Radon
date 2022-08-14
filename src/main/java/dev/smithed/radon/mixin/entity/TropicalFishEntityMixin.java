package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TropicalFishEntity.class)
public abstract class TropicalFishEntityMixin extends FishEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TropicalFishEntity entity = ((TropicalFishEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Variant":
                    nbt.putInt("Variant", entity.getVariant());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
