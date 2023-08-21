package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import dev.smithed.radon.utils.InventoriesNbtFilter;
import dev.smithed.radon.utils.NBTUtils;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin extends LockableContainerBlockEntityMixin implements ICustomNBTMixin {

    @Shadow DefaultedList<ItemStack> inventory;
    @Shadow int brewTime;
    @Shadow int fuel;
    @Shadow abstract int size();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Items" -> {
                    InventoriesNbtFilter.writeFilteredNbt(nbt, this.inventory, path);
                }
                case "BrewTime" -> nbt.putShort("BrewTime", (short) this.brewTime);
                case "Fuel" -> nbt.putByte("Fuel", (byte) this.fuel);
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
            switch (topLevelNbt) {
                case "Items" -> {
                    if(InventoriesNbtFilter.readFilteredNbt(nbt, this.inventory, path) == null) {
                        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
                        Inventories.readNbt(nbt, this.inventory);
                    }
                }
                case "BrewTime" -> this.brewTime = nbt.getShort("BrewTime");
                case "Fuel" -> this.fuel = nbt.getByte("Fuel");
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
