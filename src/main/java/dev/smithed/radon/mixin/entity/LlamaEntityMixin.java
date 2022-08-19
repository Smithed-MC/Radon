package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LlamaEntity.class)
public abstract class LlamaEntityMixin extends AbstractDonkeyEntityMixin implements ICustomNBTMixin {

    @Shadow abstract void setStrength(int strength);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        LlamaEntity entity = ((LlamaEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Variant":
                    nbt.putInt("Variant", entity.getVariant());
                    break;
                case "Strength":
                    nbt.putInt("Strength", entity.getStrength());
                    break;
                case "DecorItem":
                    if (!this.items.getStack(1).isEmpty())
                        nbt.put("DecorItem", this.items.getStack(1).writeNbt(new NbtCompound()));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        LlamaEntity entity = ((LlamaEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Strength":
                    this.setStrength(nbt.getInt("Strength"));
                    break;
                case "Variant":
                    entity.setVariant(nbt.getInt("Variant"));
                    break;
                case "DecorItem":
                    if (nbt.contains("DecorItem", 10))
                        this.items.setStack(1, ItemStack.fromNbt(nbt.getCompound("DecorItem")));
                    this.updateSaddle();
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
