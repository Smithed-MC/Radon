package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import dev.smithed.radon.mixin_interface.IEntityMixin;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements ICustomNBTMixin, IEntityMixin {
    @Final
    @Shadow
    protected BlockPos pos;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        return false;
    }

    @Override
    public NbtCompound writeFilteredNbt(NbtCompound nbt, String path) {
        String topLevelNbt = path.split("[\\.\\{\\[]")[0];
        BlockEntity entity = ((BlockEntity) (Object) this);

        switch (topLevelNbt) {
            case "x":
                nbt.putInt("x", this.pos.getX());
                break;
            case "y":
                nbt.putInt("y", this.pos.getY());
                break;
            case "z":
                nbt.putInt("z", this.pos.getZ());
                break;
            case "id":
                Identifier identifier = BlockEntityType.getId(entity.getType());
                if (identifier == null) {
                    throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
                } else {
                    nbt.putString("id", identifier.toString());
                }
                break;
            default:
                if(this.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt))
                    return nbt;
                else
                    return null;
        }
        return nbt;
    }
}
