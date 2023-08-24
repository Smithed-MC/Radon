package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        SheepEntity entity = ((SheepEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Sheared" -> nbt.putBoolean("Sheared", entity.isSheared());
                case "Color" -> nbt.putByte("Color", (byte) entity.getColor().getId());
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        SheepEntity entity = ((SheepEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "Sheared" -> entity.setSheared(nbt.getBoolean("Sheared"));
                case "Color" -> entity.setColor(DyeColor.byId(nbt.getByte("Color")));
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
