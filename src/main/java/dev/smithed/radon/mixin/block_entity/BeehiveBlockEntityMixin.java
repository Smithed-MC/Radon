package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BeehiveBlockEntity.class)
public abstract class BeehiveBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {

    @Shadow BlockPos flowerPos;
    @Shadow abstract NbtList getBees();
    @Shadow abstract boolean hasFlowerPos();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Bees" -> nbt.put("Bees", this.getBees());
                case "FlowerPos" -> {
                    if (this.hasFlowerPos())
                        nbt.put("FlowerPos", NbtHelper.fromBlockPos(this.flowerPos));
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("FlowerPos")) {
                this.flowerPos = null;
                this.flowerPos = NbtHelper.toBlockPos(nbt.getCompound("FlowerPos"));
            } else {
                return false;
            }
        }
        return true;
    }
}
