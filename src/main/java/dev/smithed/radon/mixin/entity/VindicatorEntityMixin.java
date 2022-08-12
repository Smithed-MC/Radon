package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VindicatorEntity.class)
public abstract class VindicatorEntityMixin extends RaiderEntityMixin implements ICustomNBTMixin {
    @Shadow
    boolean johnny;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        VindicatorEntity entity = ((VindicatorEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Johnny":
                    if (this.johnny)
                        nbt.putBoolean("Johnny", true);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
