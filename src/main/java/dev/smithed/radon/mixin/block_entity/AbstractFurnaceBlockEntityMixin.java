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

    @Shadow @Final Object2IntOpenHashMap<Identifier> recipesUsed;
    @Shadow DefaultedList<ItemStack> inventory;
    @Shadow int burnTime;
    @Shadow int cookTime;
    @Shadow int cookTimeTotal;
    @Shadow int fuelTime;
    @Shadow abstract int getFuelTime(ItemStack fuel);
    @Shadow abstract int size();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Items" -> Inventories.writeNbt(nbt, this.inventory);
                case "BurnTime" -> nbt.putShort("BurnTime", (short) this.burnTime);
                case "CookTime" -> nbt.putShort("CookTime", (short) this.cookTime);
                case "CookTimeTotal" -> nbt.putShort("CookTimeTotal", (short) this.cookTimeTotal);
                case "RecipesUsed" -> {
                    NbtCompound nbtCompound = new NbtCompound();
                    this.recipesUsed.forEach((identifier, count) -> {
                        nbtCompound.putInt(identifier.toString(), count);
                    });
                    nbt.put("RecipesUsed", nbtCompound);
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
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Items" -> {
                    this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
                    Inventories.readNbt(nbt, this.inventory);
                    this.fuelTime = this.getFuelTime(this.inventory.get(1));
                }
                case "BurnTime" -> this.burnTime = nbt.getShort("BurnTime");
                case "CookTime" -> this.cookTime = nbt.getShort("CookTime");
                case "CookTimeTotal" -> this.cookTimeTotal = nbt.getShort("CookTimeTotal");
                case "Tag" -> {
                    NbtCompound nbtCompound = nbt.getCompound("RecipesUsed");
                    for (String string : nbtCompound.getKeys())
                        this.recipesUsed.put(new Identifier(string), nbtCompound.getInt(string));
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
