package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SkeletonHorseEntity.class)
public abstract class SkeletonHorseEntityMixin extends AbstractHorseEntityMixin implements ICustomNBTMixin {

    @Shadow private int trapTime;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        SkeletonHorseEntity entity = ((SkeletonHorseEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "SkeletonTrap" -> nbt.putBoolean("SkeletonTrap", entity.isTrapped());
                case "SkeletonTrapTime" -> nbt.putInt("SkeletonTrapTime", this.trapTime);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        SkeletonHorseEntity entity = ((SkeletonHorseEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "SkeletonTrap" -> entity.setTrapped(nbt.getBoolean("SkeletonTrap"));
                case "SkeletonTrapTime" -> this.trapTime = nbt.getInt("SkeletonTrapTime");
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
