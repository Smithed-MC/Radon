package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
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
                //TODO: use access widener to grab id value
                //case "Variant":
                //    nbt.putInt("Variant", entity.getVariant().id);
                //    break;
                case "Strength" -> nbt.putInt("Strength", entity.getStrength());
                case "DecorItem" -> {
                    if (!this.items.getStack(1).isEmpty())
                        nbt.put("DecorItem", this.items.getStack(1).writeNbt(new NbtCompound()));
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
        LlamaEntity entity = ((LlamaEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "Strength" -> this.setStrength(nbt.getInt("Strength"));
                case "Variant" -> entity.setVariant(LlamaEntity.Variant.byId(nbt.getInt("Variant")));
                case "DecorItem" -> {
                    if (nbt.contains("DecorItem", 10))
                        this.items.setStack(1, ItemStack.fromNbt(nbt.getCompound("DecorItem")));
                    this.updateSaddle();
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
