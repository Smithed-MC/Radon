package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import dev.smithed.radon.utils.InventoriesNbtFilter;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LockableContainerBlockEntityMixin implements ICustomNBTMixin {

    @Shadow DefaultedList<ItemStack> inventory;
    @Shadow int transferCooldown;
    @Shadow abstract int size();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        final HopperBlockEntity entity = ((HopperBlockEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Items" -> {
                    if (!entity.writeLootTable(nbt)) {
                        InventoriesNbtFilter.writeFilteredNbt(nbt, this.inventory, path);
                    }
                }
                case "TransferCooldown" -> nbt.putInt("TransferCooldown", this.transferCooldown);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        final HopperBlockEntity entity = ((HopperBlockEntity) (Object) this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Items" -> {
                    if (!entity.readLootTable(nbt)) {
                        if (InventoriesNbtFilter.readFilteredNbt(nbt, this.inventory, path) == null) {
                            this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
                            Inventories.readNbt(nbt, this.inventory);
                        }
                    }
                }
                case "TransferCooldown" -> this.transferCooldown = nbt.getInt("TransferCooldown");
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
