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

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        //Minecart has 3 top level NBT fields that need to be passed together, which won't work in this case
        return super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt);
    }
}
