package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CatEntity.class)
public abstract class CatEntityMixin extends TameableEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        CatEntity entity = ((CatEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "variant" ->
                        nbt.putString("variant", Registries.CAT_VARIANT.getId(entity.getVariant()).toString());
                case "CollarColor" -> nbt.putByte("CollarColor", (byte) entity.getCollarColor().getId());
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        CatEntity entity = ((CatEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "variant" -> {
                    CatVariant catVariant = Registries.CAT_VARIANT.get(Identifier.tryParse(nbt.getString("variant")));
                    if (catVariant != null)
                        entity.setVariant(catVariant);
                }
                case "CollarColor" -> {
                    if (nbt.contains("CollarColor", 99))
                        entity.setCollarColor(DyeColor.byId(nbt.getInt("CollarColor")));
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
