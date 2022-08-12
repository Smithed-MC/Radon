package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShulkerBulletEntity.class)
public abstract class ShulkerBulletEntityMixin extends ProjectileEntityMixin implements ICustomNBTMixin {
    @Shadow
    private Entity target;
    @Shadow
    private Direction direction;
    @Shadow
    private int stepCount;
    @Shadow
    private double targetX;
    @Shadow
    private double targetY;
    @Shadow
    private double targetZ;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ShulkerBulletEntity entity = ((ShulkerBulletEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Target":
                    if (this.target != null)
                        nbt.putUuid("Target", this.target.getUuid());
                    break;
                case "Dir":
                    if (this.direction != null)
                        nbt.putInt("Dir", this.direction.getId());
                    break;
                case "Steps":
                    nbt.putInt("Steps", this.stepCount);
                    break;
                case "TXD":
                    nbt.putDouble("TXD", this.targetX);
                    break;
                case "TYD":
                    nbt.putDouble("TYD", this.targetY);
                case "TZD":
                    nbt.putDouble("TZD", this.targetZ);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
