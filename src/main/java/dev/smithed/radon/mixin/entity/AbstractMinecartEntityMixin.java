package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends EntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AbstractMinecartEntity entity = ((AbstractMinecartEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "CustomDisplayTile":
                    if (entity.hasCustomBlock())
                        nbt.putBoolean("CustomDisplayTile", true);
                    break;
                case "DisplayState":
                    if (entity.hasCustomBlock())
                        nbt.put("DisplayState", NbtHelper.fromBlockState(entity.getContainedBlock()));
                    break;
                case "DisplayOffset":
                    if (entity.hasCustomBlock())
                        nbt.putInt("DisplayOffset", entity.getBlockOffset());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
