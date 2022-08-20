package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TurtleEntity.class)
public abstract class TurtleEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Shadow abstract BlockPos getHomePos();
    @Shadow abstract BlockPos getTravelPos();
    @Shadow abstract boolean hasEgg();
    @Shadow abstract void setHasEgg(boolean hasEgg);
    @Shadow abstract void setTravelPos(BlockPos pos);

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

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TurtleEntity entity = ((TurtleEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "HasEgg":
                    this.setHasEgg(nbt.getBoolean("HasEgg"));
                    break;
                case "HomePosX":
                    int i = nbt.getInt("HomePosX");
                    entity.setHomePos(new BlockPos(i, this.getHomePos().getY(), this.getHomePos().getZ()));
                    break;
                case "HomePosY":
                    int j = nbt.getInt("HomePosY");
                    entity.setHomePos(new BlockPos(this.getHomePos().getX(), j, this.getHomePos().getZ()));
                    break;
                case "HomePosZ":
                    int k = nbt.getInt("HomePosZ");
                    entity.setHomePos(new BlockPos(this.getHomePos().getX(), this.getHomePos().getY(), k));
                    break;
                case "TravelPosX":
                    int l = nbt.getInt("TravelPosX");
                    this.setTravelPos(new BlockPos(l, this.getHomePos().getY(), this.getHomePos().getZ()));
                    break;
                case "TravelPosY":
                    int m = nbt.getInt("TravelPosY");
                    this.setTravelPos(new BlockPos(this.getHomePos().getX(), m, this.getHomePos().getZ()));
                    break;
                case "TravelPosZ":
                    int n = nbt.getInt("TravelPosZ");
                    this.setTravelPos(new BlockPos(this.getHomePos().getX(), this.getHomePos().getY(), n));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
