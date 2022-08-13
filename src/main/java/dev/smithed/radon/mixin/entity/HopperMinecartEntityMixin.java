package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HopperMinecartEntity.class)
public abstract class HopperMinecartEntityMixin extends StorageMinecartEntityMixin implements ICustomNBTMixin {
    @Shadow
    private boolean enabled = true;
    @Shadow
    private int transferCooldown = -1;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        HopperMinecartEntity entity = ((HopperMinecartEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "TransferCooldown":
                    nbt.putInt("TransferCooldown", this.transferCooldown);
                    break;
                case "Enabled":
                    nbt.putBoolean("Enabled", this.enabled);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
