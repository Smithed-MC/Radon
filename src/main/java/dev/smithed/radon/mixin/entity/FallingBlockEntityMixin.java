package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.tag.BlockTags;
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
            case "BlockState":
                nbt.put("BlockState", NbtHelper.fromBlockState(this.block));
                break;
            case "Time":
                nbt.putInt("Time", this.timeFalling);
                break;
            case "DropItem":
                nbt.putBoolean("DropItem", this.dropItem);
                break;
            case "HurtEntities":
                nbt.putBoolean("HurtEntities", this.hurtEntities);
                break;
            case "FallHurtAmount":
                nbt.putFloat("FallHurtAmount", this.fallHurtAmount);
                break;
            case "FallHurtMax":
                nbt.putInt("FallHurtMax", this.fallHurtMax);
                break;
            case "TileEntityData":
                if (entity.blockEntityData != null)
                    nbt.put("TileEntityData", entity.blockEntityData);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FallingBlockEntity entity = ((FallingBlockEntity)(Object)this);
        if(!nbt.contains(topLevelNbt))
            return false;
        switch (topLevelNbt) {
            case "BlockState":
                this.block = NbtHelper.toBlockState(nbt.getCompound("BlockState"));
                if (this.block.isAir())
                    this.block = Blocks.SAND.getDefaultState();
                break;
            case "Time":
                this.timeFalling = nbt.getInt("Time");
                break;
            case "HurtEntities":
                if (nbt.contains("HurtEntities", 99)) {
                    this.hurtEntities = nbt.getBoolean("HurtEntities");
                    this.fallHurtAmount = nbt.getFloat("FallHurtAmount");
                    this.fallHurtMax = nbt.getInt("FallHurtMax");
                } else if (this.block.isIn(BlockTags.ANVIL)) {
                    this.hurtEntities = true;
                }
                break;
            case "DropItem":
                if (nbt.contains("DropItem", 99))
                    this.dropItem = nbt.getBoolean("DropItem");
                break;
            case "TileEntityData":
                if (nbt.contains("TileEntityData", 10))
                    entity.blockEntityData = nbt.getCompound("TileEntityData");
                break;
            default:
                return false;
        }
        return true;
    }
}
