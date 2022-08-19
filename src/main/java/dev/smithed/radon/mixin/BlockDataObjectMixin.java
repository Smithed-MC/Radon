package dev.smithed.radon.mixin;

import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IDataCommandObjectMixin;
import dev.smithed.radon.mixin_interface.IEntityMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.BlockDataObject;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockDataObject.class)
public abstract class BlockDataObjectMixin implements IDataCommandObjectMixin {

    @Shadow @Final BlockEntity blockEntity;
    @Shadow @Final  BlockPos pos;
    @Shadow abstract void setNbt(NbtCompound nbt);

    @Override
    public NbtCompound getNbtFiltered(String path) {
        NbtCompound nbtCompound = null;
        if (Radon.CONFIG.nbtOptimizations && this.blockEntity instanceof IEntityMixin mixin)
            nbtCompound = mixin.writeNbtFiltered(new NbtCompound(), path);
        if(nbtCompound == null) {
            Radon.logDebug("Failed to write nbt data at " + path + " with " + this.blockEntity.getClass());
            nbtCompound = this.blockEntity.createNbtWithIdentifyingData();
        }
        return nbtCompound;
    }

    @Override
    public boolean setNbtFiltered(NbtCompound nbt, String path) {
        if(this.blockEntity instanceof IEntityMixin mixin) {
            BlockState blockState = this.blockEntity.getWorld().getBlockState(this.pos);
            if(mixin.readNbtFiltered(nbt, path)) {
                this.blockEntity.markDirty();
                this.blockEntity.getWorld().updateListeners(this.pos, blockState, blockState, 3);
                return true;
            }
        }
        Radon.logDebug("Failed to read nbt " + nbt + " at " + path + " with " + this.blockEntity.getClass());
        return false;
    }

}
