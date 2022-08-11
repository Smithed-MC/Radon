package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.ItemEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends EntityMixin implements ICustomNBTMixin {
    @Shadow
    private int itemAge;
    @Shadow
    private int pickupDelay;
    @Shadow
    private int health;

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
}
