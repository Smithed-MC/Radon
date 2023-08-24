package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FireballEntity.class)
public abstract class FireballEntityMixin extends AbstractFireballEntityMixin implements ICustomNBTMixin {

    @Shadow int explosionPower = 1;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FireballEntity entity = ((FireballEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("ExplosionPower")) {
                nbt.putByte("ExplosionPower", (byte) this.explosionPower);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FireballEntity entity = ((FireballEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            if (topLevelNbt.equals("ExplosionPower")) {
                if (nbt.contains("ExplosionPower", 99))
                    this.explosionPower = nbt.getByte("ExplosionPower");
            } else {
                return false;
            }
        }
        return true;
    }
}
