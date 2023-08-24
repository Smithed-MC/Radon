package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GhastEntity.class)
public abstract class GhastEntityMixin extends MobEntityMixin implements ICustomNBTMixin {
    @Shadow int fireballStrength = 1;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        GhastEntity entity = ((GhastEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("ExplosionPower")) {
                nbt.putByte("ExplosionPower", (byte) this.fireballStrength);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        GhastEntity entity = ((GhastEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            if (topLevelNbt.equals("ExplosionPower")) {
                if (nbt.contains("ExplosionPower", 99))
                    this.fireballStrength = nbt.getByte("ExplosionPower");
            } else {
                return false;
            }
        }
        return true;
    }
}
