package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends EntityMixin implements ICustomNBTMixin {
    @Shadow
    private BlockState block;
    @Shadow
    public int timeFalling;
    @Shadow
    public boolean dropItem;
    @Shadow
    private int fallHurtMax;
    @Shadow
    private float fallHurtAmount;
    @Shadow
    private boolean hurtEntities;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FallingBlockEntity entity = ((FallingBlockEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
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
        }
        return true;
    }
}
