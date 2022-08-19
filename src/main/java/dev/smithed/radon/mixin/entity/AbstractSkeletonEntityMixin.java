package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Shadow abstract void updateAttackType();

    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        boolean result = super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt);
        this.updateAttackType();
        return result;
    }
}
