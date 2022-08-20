package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SkeletonEntity.class)
public abstract class SkeletonEntityMixin extends AbstractSkeletonEntityMixin implements ICustomNBTMixin {

    @Shadow int conversionTime;
    @Shadow abstract void setConversionTime(int time);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        SkeletonEntity entity = ((SkeletonEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "StrayConversionTime":
                    nbt.putInt("StrayConversionTime", entity.isConverting() ? this.conversionTime : -1);
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        SkeletonEntity entity = ((SkeletonEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "StrayConversionTime":
                    if (nbt.contains("StrayConversionTime", 99) && nbt.getInt("StrayConversionTime") > -1)
                        this.setConversionTime(nbt.getInt("StrayConversionTime"));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

}
