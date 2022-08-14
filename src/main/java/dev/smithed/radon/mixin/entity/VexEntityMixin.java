package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VexEntity.class)
public abstract class VexEntityMixin extends MobEntityMixin implements ICustomNBTMixin {
    @Shadow
    private BlockPos bounds;
    @Shadow
    private boolean alive;
    @Shadow
    private int lifeTicks;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        VexEntity entity = ((VexEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "BoundX":
                    if (this.bounds != null)
                        nbt.putInt("BoundX", this.bounds.getX());
                    break;
                case "BoundY":
                    if (this.bounds != null)
                        nbt.putInt("BoundY", this.bounds.getY());
                    break;
                case "BoundZ":
                    if (this.bounds != null)
                        nbt.putInt("BoundZ", this.bounds.getZ());
                    break;
                case "LifeTicks":
                    if (this.alive)
                        nbt.putInt("LifeTicks", this.lifeTicks);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
