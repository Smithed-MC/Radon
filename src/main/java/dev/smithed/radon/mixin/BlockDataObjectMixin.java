package dev.smithed.radon.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IDataCommandObjectMixin;
import dev.smithed.radon.mixin_interface.IEntityMixin;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.BlockDataObject;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockDataObject.class)
public abstract class BlockDataObjectMixin implements IDataCommandObjectMixin {
    @Final @Shadow private BlockEntity blockEntity;
    @Shadow abstract void setNbt(NbtCompound nbt);

    @Override
    public NbtCompound getNbtFiltered(NbtPathArgumentType.NbtPath path) {
        return blockToNbtFiltered(this.blockEntity, path);
    }

    @Override
    public boolean setNbtFiltered(NbtCompound nbt, NbtPathArgumentType.NbtPath path) throws CommandSyntaxException {
        return false;
    }

    private static NbtCompound blockToNbtFiltered(BlockEntity entity, NbtPathArgumentType.NbtPath path) {
        NbtCompound nbtCompound = null;
        if (Radon.CONFIG.nbtOptimizations && entity instanceof IEntityMixin mixin)
            nbtCompound = mixin.writeNbtFiltered(new NbtCompound(), path.toString());
        if(nbtCompound == null)
            nbtCompound = entity.createNbtWithIdentifyingData();
        return nbtCompound;
    }
}
