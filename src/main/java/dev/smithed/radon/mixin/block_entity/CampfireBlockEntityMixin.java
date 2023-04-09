package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {

    @Shadow @Final DefaultedList<ItemStack> itemsBeingCooked;
    @Shadow @Final int[] cookingTimes;
    @Shadow @Final int[] cookingTotalTimes;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Items" -> Inventories.writeNbt(nbt, this.itemsBeingCooked, true);
                case "CookingTimes" -> nbt.putIntArray("CookingTimes", this.cookingTimes);
                case "CookingTotalTimes" -> nbt.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
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
            int[] is;
            switch (topLevelNbt) {
                case "Items" -> {
                    this.itemsBeingCooked.clear();
                    Inventories.readNbt(nbt, this.itemsBeingCooked);
                }
                case "CookingTimes" -> {
                    if (nbt.contains("CookingTimes", 11)) {
                        is = nbt.getIntArray("CookingTimes");
                        System.arraycopy(is, 0, this.cookingTimes, 0, Math.min(this.cookingTotalTimes.length, is.length));
                    }
                }
                case "Tag" -> {
                    if (nbt.contains("CookingTotalTimes", 11)) {
                        is = nbt.getIntArray("CookingTotalTimes");
                        System.arraycopy(is, 0, this.cookingTotalTimes, 0, Math.min(this.cookingTotalTimes.length, is.length));
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
