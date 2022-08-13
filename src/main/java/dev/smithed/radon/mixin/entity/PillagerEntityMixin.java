package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin extends RaiderEntityMixin implements ICustomNBTMixin {
    @Final
    @Shadow
    private SimpleInventory inventory;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PillagerEntity entity = ((PillagerEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Inventory":
                    NbtList nbtList = new NbtList();
                    for(int i = 0; i < this.inventory.size(); ++i) {
                        ItemStack itemStack = this.inventory.getStack(i);
                        if (!itemStack.isEmpty()) {
                            nbtList.add(itemStack.writeNbt(new NbtCompound()));
                        }
                    }
                    nbt.put("Inventory", nbtList);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
