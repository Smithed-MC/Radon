package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        EndermanEntity entity = ((EndermanEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "AngerTime", "AngryAt" -> entity.writeAngerToNbt(nbt);
                case "carriedBlockState" -> {
                    BlockState blockState = entity.getCarriedBlock();
                    if (blockState != null)
                        nbt.put("carriedBlockState", NbtHelper.fromBlockState(blockState));
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
        EndermanEntity entity = ((EndermanEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "carriedBlockState" -> {
                    BlockState blockState = null;
                    if (nbt.contains("carriedBlockState", 10)) {
                        blockState = NbtHelper.toBlockState(this.world.createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound("carriedBlockState"));
                        if (blockState.isAir()) {
                            blockState = null;
                        }
                    }
                    entity.setCarriedBlock(blockState);
                }
                case "AngryAt", "AngerTime" -> entity.readAngerFromNbt(this.world, nbt);
                default -> {
                    return false;
                }
            }

        }
        return true;
    }
}
