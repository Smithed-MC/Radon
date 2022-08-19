package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractDonkeyEntity.class)
public abstract class AbstractDonkeyEntityMixin extends AbstractHorseEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AbstractDonkeyEntity entity = ((AbstractDonkeyEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "ChestedHorse":
                    nbt.putBoolean("ChestedHorse", entity.hasChest());
                    break;
                case "Items":
                    if (entity.hasChest()) {
                        NbtList nbtList = new NbtList();

                        for (int i = 2; i < this.items.size(); ++i) {
                            ItemStack itemStack = this.items.getStack(i);
                            if (!itemStack.isEmpty()) {
                                NbtCompound nbtCompound = new NbtCompound();
                                nbtCompound.putByte("Slot", (byte) i);
                                itemStack.writeNbt(nbtCompound);
                                nbtList.add(nbtCompound);
                            }
                        }
                        nbt.put("Items", nbtList);
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
        AbstractDonkeyEntity entity = ((AbstractDonkeyEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "ChestedHorse":
                    entity.setHasChest(nbt.getBoolean("ChestedHorse"));
                    this.onChestedStatusChanged();
                    break;
                case "Items":
                    if (entity.hasChest()) {
                        NbtList nbtList = nbt.getList("Items", 10);
                        for(int i = 0; i < nbtList.size(); ++i) {
                            NbtCompound nbtCompound = nbtList.getCompound(i);
                            int j = nbtCompound.getByte("Slot") & 255;
                            if (j >= 2 && j < this.items.size()) {
                                this.items.setStack(j, ItemStack.fromNbt(nbtCompound));
                            }
                        }
                        this.updateSaddle();
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}