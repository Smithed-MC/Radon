package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import dev.smithed.radon.utils.NBTUtils;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BarrelBlockEntity.class)
public abstract class BarrelBlockEntityMixin extends LootableContainerBlockEntityMixin implements ICustomNBTMixin {
    @Shadow
    private DefaultedList<ItemStack> inventory;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Items":
                    int slot = NBTUtils.getSlot(path);
                    Radon.LOGGER.info(this.inventory.toString());
                    if (slot >= 0 && slot <= 26) {
                        ItemStack itemStack = this.inventory.get(slot);
                        if (!itemStack.isEmpty()) {
                            NbtList nbtList = new NbtList();
                            NbtCompound nbtCompound = new NbtCompound();
                            nbtCompound.putByte("Slot", (byte) slot);
                            itemStack.writeNbt(nbtCompound);
                            nbtList.add(nbtCompound);
                            nbt.put("Items", nbtList);
                        }
                    } else {
                        Inventories.writeNbt(nbt, this.inventory);
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
