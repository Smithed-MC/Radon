package dev.smithed.radon.utils;

import dev.smithed.radon.Radon;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

public class InventoriesNbtFilter {

    public static NbtCompound writeFilteredNbt(NbtCompound nbt, DefaultedList<ItemStack> stacks, String nbtPath) {
        int slot = NBTUtils.getSlot(nbtPath);

        if(slot == -1) {
            Inventories.writeNbt(nbt, stacks, true);
        } else {
            NbtList nbtList = new NbtList();

            ItemStack itemStack = stacks.get(slot);
            if (!itemStack.isEmpty()) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)slot);
                itemStack.writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
            }

            nbt.put("Items", nbtList);
        }
        return nbt;
    }


    public static NbtCompound readFilteredNbt(NbtCompound nbt, DefaultedList<ItemStack> stacks, String nbtPath) {
        int slot = NBTUtils.getSlot(nbtPath);

        if(slot == -1) {
            return null;
        } else {
            NbtList nbtList = nbt.getList("Items", 10);

            for(int i = 0; i < nbtList.size(); ++i) {
                NbtCompound nbtCompound = nbtList.getCompound(i);
                int j = nbtCompound.getByte("Slot") & 255;
                if (j < stacks.size()) {
                    stacks.set(j, ItemStack.fromNbt(nbtCompound));
                }
            }

            if(nbtList.size() == 0) {
                stacks.set(slot, ItemStack.EMPTY);
            }
        }
        return nbt;
    }

}
