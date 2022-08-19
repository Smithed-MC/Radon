package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PhantomEntity.class)
public abstract class PhantomEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Shadow BlockPos circlingCenter;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PhantomEntity entity = ((PhantomEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Size":
                    nbt.putInt("Size", entity.getPhantomSize());
                    break;
                case "AX":
                    nbt.putInt("AX", this.circlingCenter.getX());
                    break;
                case "AY":
                    nbt.putInt("AY", this.circlingCenter.getY());
                    break;
                case "AZ":
                    nbt.putInt("AZ", this.circlingCenter.getZ());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PhantomEntity entity = ((PhantomEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "AX":
                    this.circlingCenter = new BlockPos(nbt.getInt("AX"), this.circlingCenter.getY(), this.circlingCenter.getZ());
                    break;
                case "AY":
                    this.circlingCenter = new BlockPos(this.circlingCenter.getX(), nbt.getInt("AY"), this.circlingCenter.getZ());
                    break;
                case "AZ":
                    this.circlingCenter = new BlockPos(this.circlingCenter.getX(), this.circlingCenter.getY(), nbt.getInt("AZ"));
                    break;
                case "Size":
                    entity.setPhantomSize(nbt.getInt("Size"));
                    break;
                default:
                    return false;
            }

        }
        return true;
    }
}
