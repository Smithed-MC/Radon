package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MooshroomEntity.class)
public abstract class MooshroomEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Shadow StatusEffect stewEffect;
    @Shadow int stewEffectDuration;

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

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        MooshroomEntity entity = ((MooshroomEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "EffectId":
                    if (nbt.contains("EffectId", 1))
                        this.stewEffect = StatusEffect.byRawId(nbt.getInt("EffectId"));
                    break;
                case "EffectDuration":
                    if (nbt.contains("EffectDuration", 3))
                        this.stewEffectDuration = nbt.getInt("EffectDuration");
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
