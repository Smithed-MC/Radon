package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AxolotlEntity.class)
public abstract class AxolotlEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Shadow abstract void setVariant(AxolotlEntity.Variant variant);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AxolotlEntity entity = ((AxolotlEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Variant" -> nbt.putInt("Variant", entity.getVariant().getId());
                case "FromBucket" -> nbt.putBoolean("FromBucket", entity.isFromBucket());
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AxolotlEntity entity = ((AxolotlEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Variant" -> this.setVariant(AxolotlEntity.Variant.byId(nbt.getInt("Variant")));
                case "FromBucket" -> entity.setFromBucket(nbt.getBoolean("FromBucket"));
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
