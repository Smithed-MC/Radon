package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin extends AbstractDecorationEntityMixin implements ICustomNBTMixin {
    private float itemDropChance;
    private boolean fixed;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ItemFrameEntity entity = ((ItemFrameEntity)(Object)this);

        switch (topLevelNbt) {
            case "Item":
                if (!entity.getHeldItemStack().isEmpty()) {
                    nbt.put("Item", entity.getHeldItemStack().writeNbt(new NbtCompound()));
                }
                break;
            case "ItemRotation":
                if (!entity.getHeldItemStack().isEmpty()) {
                    nbt.putByte("ItemRotation", (byte)entity.getRotation());
                }
                break;
            case "ItemDropChance":
                if (!entity.getHeldItemStack().isEmpty()) {
                    nbt.putFloat("ItemDropChance", this.itemDropChance);
                }
                break;
            case "Facing":
                nbt.putByte("Facing", (byte)this.facing.getId());
                break;
            case "Invisible":
                nbt.putBoolean("Invisible", entity.isInvisible());
                break;
            case "Fixed":
                nbt.putBoolean("Fixed", this.fixed);
                break;
            default:
                return false;
        }
        return true;
    }
}
