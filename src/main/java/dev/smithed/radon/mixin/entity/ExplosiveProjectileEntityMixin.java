package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ExplosiveProjectileEntity.class)
public abstract class ExplosiveProjectileEntityMixin extends ProjectileEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ExplosiveProjectileEntity entity = ((ExplosiveProjectileEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("power")) {
                nbt.put("power", this.toNbtList(entity.powerX, entity.powerY, entity.powerZ));
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ExplosiveProjectileEntity entity = ((ExplosiveProjectileEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            if (topLevelNbt.equals("power")) {
                if (nbt.contains("power", 9)) {
                    NbtList nbtList = nbt.getList("power", 6);
                    if (nbtList.size() == 3) {
                        entity.powerX = nbtList.getDouble(0);
                        entity.powerY = nbtList.getDouble(1);
                        entity.powerZ = nbtList.getDouble(2);
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }
}
