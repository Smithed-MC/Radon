package dev.smithed.radon.mixin.entity;

import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DisplayEntity.BlockDisplayEntity.class)
public abstract class BlockDisplayEntityMixin extends DisplayEntityMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        DisplayEntity.BlockDisplayEntity entity = ((DisplayEntity.BlockDisplayEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("block_state")) {
                nbt.put("block_state", NbtHelper.fromBlockState(entity.getBlockState()));
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        DisplayEntity.BlockDisplayEntity entity = ((DisplayEntity.BlockDisplayEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            if (topLevelNbt.equals("block_state")) {
                entity.setBlockState(NbtHelper.toBlockState(this.world.createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound("block_state")));
            } else {
                return false;
            }
        }
        return true;
    }
}
