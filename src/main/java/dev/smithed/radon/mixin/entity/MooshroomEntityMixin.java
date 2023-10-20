package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.SuspiciousStewIngredient;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(MooshroomEntity.class)
public abstract class MooshroomEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Shadow
    List<SuspiciousStewIngredient.StewEffect> stewEffects;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        MooshroomEntity entity = ((MooshroomEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Type" -> nbt.putString("Type", entity.getVariant().asString());
                case "stew_effects" -> {
                    if (this.stewEffects != null) {
                        SuspiciousStewIngredient.StewEffect.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.stewEffects).result().ifPresent((nbtElement) -> {
                            nbt.put("stew_effects", nbtElement);
                        });
                    }
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
        MooshroomEntity entity = ((MooshroomEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "stew_effects" -> {
                    if (nbt.contains("stew_effects", 9)) {
                        SuspiciousStewIngredient.StewEffect.LIST_CODEC.parse(NbtOps.INSTANCE, nbt.get("stew_effects")).result().ifPresent((stewEffects) -> {
                            this.stewEffects = stewEffects;
                        });
                    }
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
