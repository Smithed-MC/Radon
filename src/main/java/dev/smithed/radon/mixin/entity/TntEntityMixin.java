package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.TntEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin extends EntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TntEntity entity = ((TntEntity) (Object) this);
        switch (topLevelNbt) {
            case "Fuse":
                nbt.putShort("Fuse", (short)entity.getFuse());
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TntEntity entity = ((TntEntity)(Object)this);
        if(!nbt.contains(topLevelNbt))
            return false;
        switch (topLevelNbt) {
            case "Fuse":
                entity.setFuse(nbt.getShort("Fuse"));
                break;
            default:
                return false;
        }
        return true;
    }
}
