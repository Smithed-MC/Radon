package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractDecorationEntity.class)
public abstract class AbstractDecorationEntityMixin extends EntityMixin implements ICustomNBTMixin {

    @Shadow @Final static Logger field_39455;
    @Shadow Direction facing;
    @Shadow BlockPos attachmentPos;
    @Shadow abstract void setFacing(Direction facing);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AbstractDecorationEntity entity = ((AbstractDecorationEntity)(Object)this);

        BlockPos blockPos;
        switch (topLevelNbt) {
            case "TileX":
                blockPos = entity.getDecorationBlockPos();
                nbt.putInt("TileX", blockPos.getX());
                break;
            case "TileY":
                blockPos = entity.getDecorationBlockPos();
                nbt.putInt("TileY", blockPos.getX());
                break;
            case "TileZ":
                blockPos = entity.getDecorationBlockPos();
                nbt.putInt("TileZ", blockPos.getX());
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AbstractDecorationEntity entity = ((AbstractDecorationEntity)(Object)this);
        if(!nbt.contains(topLevelNbt))
            return false;
        switch (topLevelNbt) {
            case "TileX":
                BlockPos blockPosX = new BlockPos(nbt.getInt("TileX"), this.attachmentPos.getY(), this.attachmentPos.getZ());
                if (!blockPosX.isWithinDistance(entity.getBlockPos(), 16.0))
                    field_39455.error("Hanging entity at invalid position: {}", blockPosX);
                else
                    this.attachmentPos = blockPosX;
                break;
            case "TileY":
                BlockPos blockPosY = new BlockPos(this.attachmentPos.getX(), nbt.getInt("TileY"), this.attachmentPos.getZ());
                if (!blockPosY.isWithinDistance(entity.getBlockPos(), 16.0))
                    field_39455.error("Hanging entity at invalid position: {}", blockPosY);
                else
                    this.attachmentPos = blockPosY;
                break;
            case "TileZ":
                BlockPos blockPosZ = new BlockPos(this.attachmentPos.getX(), this.attachmentPos.getY(), nbt.getInt("TileZ"));
                if (!blockPosZ.isWithinDistance(entity.getBlockPos(), 16.0))
                    field_39455.error("Hanging entity at invalid position: {}", blockPosZ);
                else
                    this.attachmentPos = blockPosZ;
                break;
            default:
                return false;
        }
        return true;
    }
}
