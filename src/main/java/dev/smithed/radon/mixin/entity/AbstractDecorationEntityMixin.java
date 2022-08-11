package dev.smithed.radon.mixin.entity;

import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractDecorationEntity.class)
public abstract class AbstractDecorationEntityMixin extends EntityMixin {
    @Shadow
    protected Direction facing;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, NbtPathArgumentType.NbtPath path, String topLevelNbt) {
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
}
