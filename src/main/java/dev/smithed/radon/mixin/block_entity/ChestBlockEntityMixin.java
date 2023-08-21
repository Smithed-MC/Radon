package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import dev.smithed.radon.utils.InventoriesNbtFilter;
import dev.smithed.radon.utils.NBTUtils;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityMixin extends LootableContainerBlockEntityMixin implements ICustomNBTMixin {

    @Shadow DefaultedList<ItemStack> inventory;
    @Shadow abstract int size();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Items" -> {
                    if (!this.serializeLootTable(nbt)) {
                        InventoriesNbtFilter.writeFilteredNbt(nbt, this.inventory, path);
                    }
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
            switch (topLevelNbt) {
                case "Items" -> {
                    if (!this.deserializeLootTable(nbt)) {
                        if(InventoriesNbtFilter.readFilteredNbt(nbt, this.inventory, path) == null) {
                            this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
                            Inventories.readNbt(nbt, this.inventory);
                        }
                    }
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
