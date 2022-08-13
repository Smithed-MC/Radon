package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin extends TameableEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        WolfEntity entity = ((WolfEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "CollarColor":
                    nbt.putByte("CollarColor", (byte)entity.getCollarColor().getId());
                    break;
                case "AngryAt":
                    if (entity.getAngryAt() != null)
                        nbt.putUuid("AngryAt", entity.getAngryAt());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
