package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {
    @Shadow
    protected SimpleInventory items;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AbstractHorseEntity entity = ((AbstractHorseEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "EatingHaystack":
                    nbt.putBoolean("EatingHaystack", entity.isEatingGrass());
                    break;
                case "Bred":
                    nbt.putBoolean("Bred", entity.isBred());
                    break;
                case "Temper":
                    nbt.putInt("Temper", entity.getTemper());
                    break;
                case "Tame":
                    nbt.putBoolean("Tame", entity.isTame());
                    break;
                case "Owner":
                    if (entity.getOwnerUuid() != null)
                        nbt.putUuid("Owner", entity.getOwnerUuid());
                    break;
                case "SaddleItem":
                    if (!this.items.getStack(0).isEmpty())
                        nbt.put("SaddleItem", this.items.getStack(0).writeNbt(new NbtCompound()));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
