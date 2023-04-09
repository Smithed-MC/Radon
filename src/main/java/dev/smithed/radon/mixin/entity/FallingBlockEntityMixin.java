package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends EntityMixin implements ICustomNBTMixin {

    @Shadow BlockState block;
    @Shadow int timeFalling;
    @Shadow boolean dropItem;
    @Shadow int fallHurtMax;
    @Shadow float fallHurtAmount;
    @Shadow boolean hurtEntities;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FallingBlockEntity entity = ((FallingBlockEntity) (Object) this);
        switch (topLevelNbt) {
            case "BlockState" -> nbt.put("BlockState", NbtHelper.fromBlockState(this.block));
            case "Time" -> nbt.putInt("Time", this.timeFalling);
            case "DropItem" -> nbt.putBoolean("DropItem", this.dropItem);
            case "HurtEntities" -> nbt.putBoolean("HurtEntities", this.hurtEntities);
            case "FallHurtAmount" -> nbt.putFloat("FallHurtAmount", this.fallHurtAmount);
            case "FallHurtMax" -> nbt.putInt("FallHurtMax", this.fallHurtMax);
            case "TileEntityData" -> {
                if (entity.blockEntityData != null)
                    nbt.put("TileEntityData", entity.blockEntityData);
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FallingBlockEntity entity = ((FallingBlockEntity)(Object)this);
        if(!nbt.contains(topLevelNbt))
            return false;
        switch (topLevelNbt) {
            case "BlockState" -> {
                this.block = NbtHelper.toBlockState(this.world.createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound("BlockState"));
                if (this.block.isAir())
                    this.block = Blocks.SAND.getDefaultState();
            }
            case "Time" -> this.timeFalling = nbt.getInt("Time");
            case "HurtEntities" -> {
                if (nbt.contains("HurtEntities", 99)) {
                    this.hurtEntities = nbt.getBoolean("HurtEntities");
                    this.fallHurtAmount = nbt.getFloat("FallHurtAmount");
                    this.fallHurtMax = nbt.getInt("FallHurtMax");
                } else if (this.block.isIn(BlockTags.ANVIL)) {
                    this.hurtEntities = true;
                }
            }
            case "DropItem" -> {
                if (nbt.contains("DropItem", 99))
                    this.dropItem = nbt.getBoolean("DropItem");
            }
            case "TileEntityData" -> {
                if (nbt.contains("TileEntityData", 10))
                    entity.blockEntityData = nbt.getCompound("TileEntityData");
            }
            default -> {
                return false;
            }
        }
        return true;
    }
}
