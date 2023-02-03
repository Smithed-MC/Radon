package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HorseEntity.class)
public abstract class HorseEntityMixin extends AbstractHorseEntityMixin implements ICustomNBTMixin {

    @Shadow abstract int getHorseVariant();
    @Shadow abstract void setHorseVariant(int variant);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        HorseEntity entity = ((HorseEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Variant":
                    nbt.putInt("Variant", this.getHorseVariant());
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

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        HorseEntity entity = ((HorseEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Variant":
                    this.setHorseVariant(nbt.getInt("Variant"));
                    break;
                case "ArmorItem":
                    if (nbt.contains("ArmorItem", 10)) {
                        ItemStack itemStack = ItemStack.fromNbt(nbt.getCompound("ArmorItem"));
                        if (!itemStack.isEmpty() && entity.isHorseArmor(itemStack)) {
                            this.items.setStack(1, itemStack);
                        }
                        this.updateSaddle();
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
