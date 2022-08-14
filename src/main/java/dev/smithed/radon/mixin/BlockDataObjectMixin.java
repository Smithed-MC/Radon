package dev.smithed.radon.mixin;

import dev.smithed.radon.Radon;
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
public class BlockDataObjectMixin implements DataCommandObjectMixin {
    @Final
    @Shadow
    private BlockEntity blockEntity;

    public NbtCompound getFilteredNbt(NbtPathArgumentType.NbtPath path) {
        return this.blockToNbtFiltered(this.blockEntity, path);
    }

    private static NbtCompound blockToNbtFiltered(BlockEntity entity, NbtPathArgumentType.NbtPath path) {
        NbtCompound nbtCompound = null;
        if (Radon.CONFIG.nbtOptimizations && entity instanceof IEntityMixin mixin)
            nbtCompound = mixin.writeFilteredNbt(new NbtCompound(), path.toString());
        if(nbtCompound == null)
            nbtCompound = entity.createNbtWithIdentifyingData();
        return nbtCompound;
    }
}
