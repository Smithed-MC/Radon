package dev.smithed.radon.mixin.misc;

import dev.smithed.radon.mixin_interface.IFilteredNbtList;
import dev.smithed.radon.utils.NBTUtils;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnderChestInventory.class)
public abstract class EnderChestInventoryMixin extends SimpleInventory implements IFilteredNbtList {

    @Shadow
    public abstract NbtList toNbtList();

    @Override
    public NbtList writeNbtFiltered(NbtList nbtList, String nbt) {
        int slot = NBTUtils.getSlot(nbt);
        if(slot == -1) {
            nbtList = this.toNbtList();
        } else {
            if(slot >= 0 && slot <= 35) {
                ItemStack itemStack = this.getStack(slot);
                if (!itemStack.isEmpty()) {
                    NbtCompound nbtCompound = new NbtCompound();
                    nbtCompound.putByte("Slot", (byte)slot);
                    itemStack.writeNbt(nbtCompound);
                    nbtList.add(nbtCompound);
                }
            }
        }
        return nbtList;
    }
}
