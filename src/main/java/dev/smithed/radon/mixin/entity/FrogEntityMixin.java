package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.FrogVariant;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FrogEntity.class)
public abstract class FrogEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FrogEntity entity = ((FrogEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("variant")) {
                nbt.putString("variant", Registries.FROG_VARIANT.getId(entity.getVariant()).toString());
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FrogEntity entity = ((FrogEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            if (topLevelNbt.equals("variant")) {
                FrogVariant frogVariant = Registries.FROG_VARIANT.get(Identifier.tryParse(nbt.getString("variant")));
                if (frogVariant != null)
                    entity.setVariant(frogVariant);
            } else {
                return false;
            }
        }
        return true;
    }
}
