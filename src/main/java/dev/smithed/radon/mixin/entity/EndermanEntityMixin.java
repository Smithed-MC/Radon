package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        EndermanEntity entity = ((EndermanEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "AngerTime":
                    nbt.putInt("AngerTime", entity.getAngerTime());
                    break;
                case "AngryAt":
                    if (entity.getAngryAt() != null)
                        nbt.putUuid("AngryAt", entity.getAngryAt());
                    break;
                case "carriedBlockState":
                    BlockState blockState = entity.getCarriedBlock();
                    if (blockState != null)
                        nbt.put("carriedBlockState", NbtHelper.fromBlockState(blockState));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
