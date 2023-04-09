package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin extends TameableEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        WolfEntity entity = ((WolfEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "CollarColor" -> nbt.putByte("CollarColor", (byte) entity.getCollarColor().getId());
                case "AngryAt", "AngerTime" -> entity.writeAngerToNbt(nbt);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        WolfEntity entity = ((WolfEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "CollarColor" -> {
                    if (nbt.contains("CollarColor", 99))
                        entity.setCollarColor(DyeColor.byId(nbt.getInt("CollarColor")));
                }
                case "AngryAt", "AngerTime" -> entity.readAngerFromNbt(this.world, nbt);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
