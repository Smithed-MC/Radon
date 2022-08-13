package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GhastEntity.class)
public abstract class GhastEntityMixin extends MobEntityMixin implements ICustomNBTMixin {
    @Shadow
    private int fireballStrength = 1;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        GhastEntity entity = ((GhastEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "ExplosionPower":
                    nbt.putByte("ExplosionPower", (byte)this.fireballStrength);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
