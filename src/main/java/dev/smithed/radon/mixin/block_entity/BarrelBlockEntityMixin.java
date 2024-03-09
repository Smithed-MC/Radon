package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import dev.smithed.radon.utils.InventoriesNbtFilter;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BarrelBlockEntity.class)
public abstract class BarrelBlockEntityMixin extends LockableContainerBlockEntityMixin implements ICustomNBTMixin {

    @Shadow DefaultedList<ItemStack> inventory;
    @Shadow abstract int size();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        final BarrelBlockEntity entity = ((BarrelBlockEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("Items")) {
                if (!entity.writeLootTable(nbt))
                    InventoriesNbtFilter.writeFilteredNbt(nbt, this.inventory, path);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        final BarrelBlockEntity entity = ((BarrelBlockEntity) (Object) this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("Items")) {
                if (!entity.readLootTable(nbt)) {
                    if (InventoriesNbtFilter.readFilteredNbt(nbt, this.inventory, path) == null) {
                        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
                        Inventories.readNbt(nbt, this.inventory);
                    }
                }
                return true;
            }
        }
        return false;
    }
}
