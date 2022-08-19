package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;

@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandEntityMixin extends LivingEntityMixin implements ICustomNBTMixin {

    @Shadow int disabledSlots;
    @Shadow DefaultedList<ItemStack> heldItems;
    @Shadow DefaultedList<ItemStack> armorItems;
    @Shadow abstract NbtCompound poseToNbt();
    @Shadow abstract void setSmall(boolean small);
    @Shadow abstract void setShowArms(boolean showArms);
    @Shadow abstract void setHideBasePlate(boolean hideBasePlate);
    @Shadow abstract void setMarker(boolean marker);
    @Shadow abstract void readPoseNbt(NbtCompound nbt);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ArmorStandEntity entity = ((ArmorStandEntity)(Object)this);
        if(!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "ArmorItems":
                    NbtList nbtList = new NbtList();
                    NbtCompound nbtCompound;
                    for (Iterator var3 = this.armorItems.iterator(); var3.hasNext(); nbtList.add(nbtCompound)) {
                        ItemStack itemStack = (ItemStack) var3.next();
                        nbtCompound = new NbtCompound();
                        if (!itemStack.isEmpty()) {
                            itemStack.writeNbt(nbtCompound);
                        }
                    }
                    nbt.put("ArmorItems", nbtList);
                    break;
                case "HandItems":
                    NbtList nbtList2 = new NbtList();
                    NbtCompound nbtCompound2;
                    for (Iterator var8 = this.heldItems.iterator(); var8.hasNext(); nbtList2.add(nbtCompound2)) {
                        ItemStack itemStack2 = (ItemStack) var8.next();
                        nbtCompound2 = new NbtCompound();
                        if (!itemStack2.isEmpty()) {
                            itemStack2.writeNbt(nbtCompound2);
                        }
                    }
                    nbt.put("HandItems", nbtList2);
                    break;
                case "Invisible":
                    nbt.putBoolean("Invisible", entity.isInvisible());
                    break;
                case "Small":
                    nbt.putBoolean("Small", entity.isSmall());
                    break;
                case "ShowArms":
                    nbt.putBoolean("ShowArms", entity.shouldShowArms());
                    break;
                case "DisabledSlots":
                    nbt.putInt("DisabledSlots", this.disabledSlots);
                    break;
                case "NoBasePlate":
                    nbt.putBoolean("NoBasePlate", entity.shouldHideBasePlate());
                    break;
                case "Marker":
                    if (entity.isMarker()) {
                        nbt.putBoolean("Marker", entity.isMarker());
                    }
                    break;
                case "Pose":
                    nbt.put("Pose", this.poseToNbt());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ArmorStandEntity entity = ((ArmorStandEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            NbtList nbtList;
            switch (topLevelNbt) {
                case "ArmorItems":
                    if (nbt.contains("ArmorItems", 9)) {
                        nbtList = nbt.getList("ArmorItems", 10);
                        for (int i = 0; i < this.armorItems.size(); ++i)
                            this.armorItems.set(i, ItemStack.fromNbt(nbtList.getCompound(i)));
                    }
                    break;
                case "HandItems":
                    if (nbt.contains("HandItems", 9)) {
                        nbtList = nbt.getList("HandItems", 10);
                        for (int i = 0; i < this.heldItems.size(); ++i)
                            this.heldItems.set(i, ItemStack.fromNbt(nbtList.getCompound(i)));
                    }
                    break;
                case "Invisible":
                    entity.setInvisible(nbt.getBoolean("Invisible"));
                    break;
                case "Small":
                    this.setSmall(nbt.getBoolean("Small"));
                    break;
                case "ShowArms":
                    this.setShowArms(nbt.getBoolean("ShowArms"));
                    break;
                case "DisabledSlots":
                    this.disabledSlots = nbt.getInt("DisabledSlots");
                    break;
                case "NoBasePlate":
                    this.setHideBasePlate(nbt.getBoolean("NoBasePlate"));
                    break;
                case "Marker":
                    this.setMarker(nbt.getBoolean("Marker"));
                    break;
                case "Pose":
                    NbtCompound nbtCompound = nbt.getCompound("Pose");
                    this.readPoseNbt(nbtCompound);
                    break;
                default:
                    return false;
            }

        }
        return true;
    }
}
