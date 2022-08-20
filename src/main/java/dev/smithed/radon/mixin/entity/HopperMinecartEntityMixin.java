package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HopperMinecartEntity.class)
public abstract class HopperMinecartEntityMixin extends StorageMinecartEntityMixin implements ICustomNBTMixin {
    @Shadow boolean enabled = true;
    @Shadow int transferCooldown = -1;

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

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        HopperMinecartEntity entity = ((HopperMinecartEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "TransferCooldown":
                    this.transferCooldown = nbt.getInt("TransferCooldown");
                    break;
                case "Enabled":
                    this.enabled = nbt.getBoolean("Enabled");
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
