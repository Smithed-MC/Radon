package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VexEntity.class)
public abstract class VexEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Shadow BlockPos bounds;
    @Shadow boolean alive;
    @Shadow int lifeTicks;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        VexEntity entity = ((VexEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "BoundX" -> {
                    if (this.bounds != null)
                        nbt.putInt("BoundX", this.bounds.getX());
                }
                case "BoundY" -> {
                    if (this.bounds != null)
                        nbt.putInt("BoundY", this.bounds.getY());
                }
                case "BoundZ" -> {
                    if (this.bounds != null)
                        nbt.putInt("BoundZ", this.bounds.getZ());
                }
                case "LifeTicks" -> {
                    if (this.alive)
                        nbt.putInt("LifeTicks", this.lifeTicks);
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        VexEntity entity = ((VexEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "LifeTicks" -> entity.setLifeTicks(nbt.getInt("LifeTicks"));
                case "BoundX" -> {
                    int i = nbt.getInt("BoundX");
                    this.bounds = new BlockPos(i, this.bounds.getY(), this.bounds.getZ());
                }
                case "BoundY" -> {
                    int j = nbt.getInt("BoundY");
                    this.bounds = new BlockPos(this.bounds.getX(), j, this.bounds.getZ());
                }
                case "BoundZ" -> {
                    int k = nbt.getInt("BoundZ");
                    this.bounds = new BlockPos(this.bounds.getX(), this.bounds.getY(), k);
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
