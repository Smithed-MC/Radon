package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin extends AbstractDecorationEntityMixin implements ICustomNBTMixin {

    @Shadow @Final static Logger ITEM_FRAME_LOGGER;
    @Shadow float itemDropChance;
    @Shadow boolean fixed;
    @Shadow abstract void setRotation(int value, boolean updateComparators);
    @Shadow abstract void setFacing(Direction facing);
    @Shadow abstract void removeFromFrame(ItemStack itemStack);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ItemFrameEntity entity = ((ItemFrameEntity)(Object)this);

        switch (topLevelNbt) {
            case "Item" -> {
                if (!entity.getHeldItemStack().isEmpty()) {
                    nbt.put("Item", entity.getHeldItemStack().writeNbt(new NbtCompound()));
                }
            }
            case "ItemRotation" -> {
                if (!entity.getHeldItemStack().isEmpty()) {
                    nbt.putByte("ItemRotation", (byte) entity.getRotation());
                }
            }
            case "ItemDropChance" -> {
                if (!entity.getHeldItemStack().isEmpty()) {
                    nbt.putFloat("ItemDropChance", this.itemDropChance);
                }
            }
            case "Facing" -> nbt.putByte("Facing", (byte) this.facing.getId());
            case "Invisible" -> nbt.putBoolean("Invisible", entity.isInvisible());
            case "Fixed" -> nbt.putBoolean("Fixed", this.fixed);
            default -> {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ItemFrameEntity entity = ((ItemFrameEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "Item" -> {
                    NbtCompound nbtCompound = nbt.getCompound("Item");
                    if (nbtCompound != null && !nbtCompound.isEmpty()) {
                        ItemStack itemStack = ItemStack.fromNbt(nbtCompound);
                        if (itemStack.isEmpty())
                            ITEM_FRAME_LOGGER.warn("Unable to load item from: {}", nbtCompound);
                        ItemStack itemStack2 = entity.getHeldItemStack();
                        if (!itemStack2.isEmpty() && !ItemStack.areEqual(itemStack, itemStack2))
                            this.removeFromFrame(itemStack2);
                        entity.setHeldItemStack(itemStack, false);
                    }
                }
                case "ItemRotation" -> this.setRotation(nbt.getByte("ItemRotation"), false);
                case "ItemDropChance" -> {
                    if (nbt.contains("ItemDropChance", 99))
                        this.itemDropChance = nbt.getFloat("ItemDropChance");
                }
                case "Facing" -> this.setFacing(Direction.byId(nbt.getByte("Facing")));
                case "Invisible" -> entity.setInvisible(nbt.getBoolean("Invisible"));
                case "Fixed" -> this.fixed = nbt.getBoolean("Fixed");
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
