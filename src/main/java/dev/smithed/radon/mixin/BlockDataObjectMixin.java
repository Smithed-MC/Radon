package dev.smithed.radon.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.BlockDataObject;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockDataObject.class)
public class BlockDataObjectMixin {
    @Final
    @Shadow
    private BlockEntity blockEntity;

    public NbtCompound getFilteredNbt(NbtPathArgumentType.NbtPath path) {
        return this.blockEntity.createNbtWithIdentifyingData();
    }
}
