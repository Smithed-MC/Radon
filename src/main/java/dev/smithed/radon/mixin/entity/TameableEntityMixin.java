package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TameableEntity.class)
public abstract class TameableEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {
    @Shadow
    private boolean sitting;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TameableEntity entity = ((TameableEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Sitting":
                    nbt.putBoolean("Sitting", this.sitting);
                    break;
                case "Owner":
                    if (entity.getOwnerUuid() != null)
                        nbt.putUuid("Owner", entity.getOwnerUuid());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
