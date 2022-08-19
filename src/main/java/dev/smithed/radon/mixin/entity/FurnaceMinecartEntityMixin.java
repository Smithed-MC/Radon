package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FurnaceMinecartEntity.class)
public abstract class FurnaceMinecartEntityMixin extends AbstractMinecartEntityMixin implements ICustomNBTMixin {
    @Shadow int fuel;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FurnaceMinecartEntity entity = ((FurnaceMinecartEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "PushX":
                    nbt.putDouble("PushX", entity.pushX);
                    break;
                case "PushZ":
                    nbt.putDouble("PushZ", entity.pushZ);
                    break;
                case "Fuel":
                    nbt.putShort("Fuel", (short)this.fuel);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FurnaceMinecartEntity entity = ((FurnaceMinecartEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "PushX":
                    entity.pushX = nbt.getDouble("PushX");
                    break;
                case "PushZ":
                    entity.pushZ = nbt.getDouble("PushZ");
                    break;
                case "Fuel":
                    this.fuel = nbt.getShort("Fuel");
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
