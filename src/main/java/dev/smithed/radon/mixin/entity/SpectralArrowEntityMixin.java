package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SpectralArrowEntity.class)
public abstract class SpectralArrowEntityMixin extends PersistentProjectileEntityMixin implements ICustomNBTMixin {
    @Shadow
    private int duration;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        SpectralArrowEntity entity = ((SpectralArrowEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Duration":
                    nbt.putInt("Duration", this.duration);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
