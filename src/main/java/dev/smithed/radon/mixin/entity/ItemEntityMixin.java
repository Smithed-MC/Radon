package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends EntityMixin implements ICustomNBTMixin {

    @Shadow int itemAge;
    @Shadow int pickupDelay;
    @Shadow int health;
    @Shadow UUID thrower;
    @Shadow UUID owner;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ItemEntity entity = ((ItemEntity)(Object)this);

        switch (topLevelNbt) {
            case "Health":
                nbt.putShort("Health", (short)this.health);
                break;
            case "Age":
                nbt.putShort("Age", (short)this.itemAge);
                break;
            case "PickupDelay":
                nbt.putShort("PickupDelay", (short)this.pickupDelay);
                break;
            case "Thrower":
                if (entity.getThrower() != null) {
                    nbt.putUuid("Thrower", entity.getThrower());
                }
                break;
            case "Owner":
                if (entity.getOwner() != null) {
                    nbt.putUuid("Owner", entity.getOwner());
                }
            case "Item":
                if (!entity.getStack().isEmpty()) {
                    nbt.put("Item", entity.getStack().writeNbt(new NbtCompound()));
                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ItemEntity entity = ((ItemEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Health":
                    this.health = nbt.getShort("Health");
                    break;
                case "Age":
                    this.itemAge = nbt.getShort("Age");
                    break;
                case "PickupDelay":
                    this.pickupDelay = nbt.getShort("PickupDelay");
                    break;
                case "Owner":
                    this.owner = nbt.getUuid("Owner");
                    break;
                case "Thrower":
                    this.thrower = nbt.getUuid("Thrower");
                    break;
                case "Item":
                    NbtCompound nbtCompound = nbt.getCompound("Item");
                    entity.setStack(ItemStack.fromNbt(nbtCompound));
                    if (entity.getStack().isEmpty()) {
                        entity.discard();
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
