package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends LockableContainerBlockEntityMixin implements ICustomNBTMixin {
    @Shadow
    private DefaultedList<ItemStack> inventory;
    @Final
    @Shadow
    private Object2IntOpenHashMap<Identifier> recipesUsed;
    @Shadow
    int burnTime;
    @Shadow
    int cookTime;
    @Shadow
    int cookTimeTotal;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Items":
                    Inventories.writeNbt(nbt, this.inventory);
                    break;
                case "BurnTime":
                    nbt.putShort("BurnTime", (short)this.burnTime);
                    break;
                case "CookTime":
                    nbt.putShort("CookTime", (short)this.cookTime);
                    break;
                case "CookTimeTotal":
                    nbt.putShort("CookTimeTotal", (short)this.cookTimeTotal);
                    break;
                case "RecipesUsed":
                    NbtCompound nbtCompound = new NbtCompound();
                    this.recipesUsed.forEach((identifier, count) -> {
                        nbtCompound.putInt(identifier.toString(), count);
                    });
                    nbt.put("RecipesUsed", nbtCompound);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
