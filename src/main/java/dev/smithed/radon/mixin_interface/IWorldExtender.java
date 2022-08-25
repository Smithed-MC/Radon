package dev.smithed.radon.mixin_interface;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public interface IWorldExtender {

    BlockState getBlockStateNoLoad(BlockPos pos);
    BlockEntity getBlockEntityNoLoad(BlockPos pos);

}
