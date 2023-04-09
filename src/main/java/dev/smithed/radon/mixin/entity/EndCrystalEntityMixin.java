package dev.smithed.radon.mixin.entity;

import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EndCrystalEntity.class)
public abstract class EndCrystalEntityMixin extends EntityMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        EndCrystalEntity entity = ((EndCrystalEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "ShowBottom" -> nbt.putBoolean("ShowBottom", entity.shouldShowBottom());
                case "BeamTarget" -> {
                    if (entity.getBeamTarget() != null)
                        nbt.put("BeamTarget", NbtHelper.fromBlockPos(entity.getBeamTarget()));
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
        EndCrystalEntity entity = ((EndCrystalEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "BeamTarget" -> {
                    if (nbt.contains("BeamTarget", 10))
                        entity.setBeamTarget(NbtHelper.toBlockPos(nbt.getCompound("BeamTarget")));
                }
                case "ShowBottom" -> {
                    if (nbt.contains("ShowBottom", 1))
                        entity.setShowBottom(nbt.getBoolean("ShowBottom"));
                }
                default -> {
                    return false;
                }
            }

        }
        return true;
    }
}
