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
                case "PushX" -> nbt.putDouble("PushX", entity.pushX);
                case "PushZ" -> nbt.putDouble("PushZ", entity.pushZ);
                case "Fuel" -> nbt.putShort("Fuel", (short) this.fuel);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FurnaceMinecartEntity entity = ((FurnaceMinecartEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "PushX" -> entity.pushX = nbt.getDouble("PushX");
                case "PushZ" -> entity.pushZ = nbt.getDouble("PushZ");
                case "Fuel" -> this.fuel = nbt.getShort("Fuel");
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
