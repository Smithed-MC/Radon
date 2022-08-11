package dev.smithed.radon.mixin.misc;

import dev.smithed.radon.mixin_interface.IFilteredNbtList;
import dev.smithed.radon.utils.NBTUtils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements IFilteredNbtList {

    @Shadow
    public DefaultedList<ItemStack> main;
    @Shadow
    public DefaultedList<ItemStack> armor;
    @Shadow
    public DefaultedList<ItemStack> offHand;
    @Shadow
    public abstract NbtList writeNbt(NbtList nbtList);

    @Override
    public NbtList writeNbtFiltered(NbtList nbtList, String nbt) {
        int slot = NBTUtils.getSlot(nbt);
        if(slot == -1) {
            this.writeNbt(nbtList);
        } else {
            if(slot >= 0 && slot <= 35) {
                if (!(this.main.get(slot)).isEmpty()) {
                    NbtCompound nbtCompound = new NbtCompound();
                    nbtCompound.putByte("Slot", (byte) slot);
                    (this.main.get(slot)).writeNbt(nbtCompound);
                    nbtList.add(nbtCompound);
                }
            }

            if(slot >= 100 && slot <= 103) {
                if (!(this.armor.get(slot-100)).isEmpty()) {
                    NbtCompound nbtCompound = new NbtCompound();
                    nbtCompound.putByte("Slot", (byte) slot);
                    (this.armor.get(slot-100)).writeNbt(nbtCompound);
                    nbtList.add(nbtCompound);
                }
            }

            if(slot == -106) {
                if (!(this.offHand.get(0)).isEmpty()) {
                    NbtCompound nbtCompound = new NbtCompound();
                    nbtCompound.putByte("Slot", (byte) -106);
                    (this.offHand.get(0)).writeNbt(nbtCompound);
                    nbtList.add(nbtCompound);
                }
            }
        }
        return nbtList;
    }

}
