package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TurtleEntity.class)
public abstract class TurtleEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {
    @Shadow
    abstract BlockPos getHomePos();
    @Shadow
    abstract BlockPos getTravelPos();
    @Shadow
    abstract boolean hasEgg();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TurtleEntity entity = ((TurtleEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "HomePosX":
                    nbt.putInt("HomePosX", this.getHomePos().getX());
                    break;
                case "HomePosY":
                    nbt.putInt("HomePosY", this.getHomePos().getY());
                    break;
                case "HomePosZ":
                    nbt.putInt("HomePosZ", this.getHomePos().getZ());
                    break;
                case "TravelPosX":
                    nbt.putInt("TravelPosX", this.getTravelPos().getX());
                    break;
                case "TravelPosY":
                    nbt.putInt("TravelPosY", this.getTravelPos().getY());
                    break;
                case "TravelPosZ":
                    nbt.putInt("TravelPosZ", this.getTravelPos().getZ());
                    break;
                case "HasEgg":
                    nbt.putBoolean("HasEgg", this.hasEgg());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
