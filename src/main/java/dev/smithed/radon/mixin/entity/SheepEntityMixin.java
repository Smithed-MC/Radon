package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        SheepEntity entity = ((SheepEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Sheared":
                    nbt.putBoolean("Sheared", entity.isSheared());
                    break;
                case "Color":
                    nbt.putByte("Color", (byte)entity.getColor().getId());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
