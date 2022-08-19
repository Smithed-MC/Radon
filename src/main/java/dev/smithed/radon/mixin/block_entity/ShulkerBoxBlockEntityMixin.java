package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import dev.smithed.radon.utils.NBTUtils;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityMixin extends LootableContainerBlockEntityMixin implements ICustomNBTMixin {

    @Shadow DefaultedList<ItemStack> inventory;
    @Shadow abstract int size();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Items":
                    if (this.lootTableId == null) {
                        int slot = NBTUtils.getSlot(path);
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
                    }
                    break;
                case "LootTable":
                case "LootTableSeed":
                    if (this.lootTableId != null) {
                        nbt.putString("LootTable", this.lootTableId.toString());
                        if (this.lootTableSeed != 0L)
                            nbt.putLong("LootTableSeed", this.lootTableSeed);
                    }
                    break;
                default:
                    return false;
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
                case "Items":
                    this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
                    Inventories.readNbt(nbt, this.inventory);
                    break;
                case "LootTable":
                case "LootTableSeed":
                    if (nbt.contains("LootTable", 8)) {
                        this.lootTableId = new Identifier(nbt.getString("LootTable"));
                        this.lootTableSeed = nbt.getLong("LootTableSeed");
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
