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
                case "MainGene" -> nbt.putString("MainGene", entity.getMainGene().asString());
                case "HiddenGene" -> nbt.putString("HiddenGene", entity.getHiddenGene().asString());
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PandaEntity entity = ((PandaEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "MainGene" -> entity.setMainGene(PandaEntity.Gene.byName(nbt.getString("MainGene")));
                case "HiddenGene" -> entity.setHiddenGene(PandaEntity.Gene.byName(nbt.getString("HiddenGene")));
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
