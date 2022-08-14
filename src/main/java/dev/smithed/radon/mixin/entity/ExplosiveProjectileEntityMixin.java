package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ExplosiveProjectileEntity.class)
public abstract class ExplosiveProjectileEntityMixin extends ProjectileEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ExplosiveProjectileEntity entity = ((ExplosiveProjectileEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "power":
                    nbt.put("power", this.toNbtList(new double[]{entity.powerX, entity.powerY, entity.powerZ}));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
