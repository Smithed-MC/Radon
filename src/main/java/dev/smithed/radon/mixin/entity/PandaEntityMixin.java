package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PandaEntity.class)
public abstract class PandaEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PandaEntity entity = ((PandaEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "MainGene":
                    nbt.putString("MainGene", entity.getMainGene().getName());
                    break;
                case "HiddenGene":
                    nbt.putString("HiddenGene", entity.getHiddenGene().getName());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
