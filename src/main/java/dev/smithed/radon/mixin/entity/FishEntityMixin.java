package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FishEntity.class)
public abstract class FishEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FishEntity entity = ((FishEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("FromBucket")) {
                nbt.putBoolean("FromBucket", entity.isFromBucket());
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FishEntity entity = ((FishEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            if (topLevelNbt.equals("FromBucket")) {
                entity.setFromBucket(nbt.getBoolean("FromBucket"));
            } else {
                return false;
            }
        }
        return true;
    }
}
