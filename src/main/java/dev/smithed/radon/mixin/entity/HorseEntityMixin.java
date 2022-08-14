package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HorseEntity.class)
public abstract class HorseEntityMixin extends AbstractHorseEntityMixin implements ICustomNBTMixin {
    @Shadow
    abstract int getVariant();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        HorseEntity entity = ((HorseEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Variant":
                    nbt.putInt("Variant", this.getVariant());
                    break;
                case "ArmorItem":
                    if (!this.items.getStack(1).isEmpty())
                        nbt.put("ArmorItem", this.items.getStack(1).writeNbt(new NbtCompound()));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
