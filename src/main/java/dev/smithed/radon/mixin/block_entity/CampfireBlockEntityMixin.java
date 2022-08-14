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
    @Final
    @Shadow
    private DefaultedList<ItemStack> itemsBeingCooked;
    @Final
    @Shadow
    private int[] cookingTimes;
    @Final
    @Shadow
    private int[] cookingTotalTimes;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Items":
                    Inventories.writeNbt(nbt, this.itemsBeingCooked, true);
                    break;
                case "CookingTimes":
                    nbt.putIntArray("CookingTimes", this.cookingTimes);
                    break;
                case "CookingTotalTimes":
                    nbt.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
