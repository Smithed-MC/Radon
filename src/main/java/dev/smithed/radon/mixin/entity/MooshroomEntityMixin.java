package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MooshroomEntity.class)
public abstract class MooshroomEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {
    @Shadow
    private StatusEffect stewEffect;
    @Shadow
    private int stewEffectDuration;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        MooshroomEntity entity = ((MooshroomEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Type":
                    //the name field of getMooshroomType cannot be accessed directly, so here we get the enum
                    //name and make it lower case. It's the same result, but could theoretically break.
                    nbt.putString("Type", entity.getMooshroomType().toString().toLowerCase());
                    break;
                case "EffectId":
                    if (this.stewEffect != null)
                        nbt.putInt("EffectId", StatusEffect.getRawId(this.stewEffect));
                    break;
                case "EffectDuration":
                    if (this.stewEffect != null)
                        nbt.putInt("EffectDuration", this.stewEffectDuration);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
