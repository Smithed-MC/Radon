package dev.smithed.radon.mixin.entity;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import dev.smithed.radon.mixin_interface.IFilteredNbtList;
import net.minecraft.SharedConstants;
import net.minecraft.block.entity.SculkShriekerWarningManager;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.dynamic.DynamicSerializableUuid;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.random.Random;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin implements ICustomNBTMixin {

    @Shadow PlayerInventory inventory;
    @Shadow int sleepTimer;
    @Shadow int enchantmentTableSeed;
    @Shadow HungerManager hungerManager;
    @Shadow PlayerAbilities abilities;
    @Shadow EnderChestInventory enderChestInventory;
    @Shadow abstract void setShoulderEntityRight(NbtCompound entityNbt);
    @Shadow abstract void setShoulderEntityLeft(NbtCompound entityNbt);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PlayerEntity entity = ((PlayerEntity)(Object)this);

        if(!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "DataVersion":
                    nbt.putInt("DataVersion", SharedConstants.getGameVersion().getSaveVersion().getId());
                    break;
                case "Inventory":
                    if(this.inventory instanceof IFilteredNbtList mixin)
                        nbt.put("Inventory", mixin.writeNbtFiltered(new NbtList(), path.toString()));
                    else
                        nbt.put("Inventory", this.inventory.writeNbt(new NbtList()));
                    break;
                case "SelectedItemSlot":
                    nbt.putInt("SelectedItemSlot", this.inventory.selectedSlot);
                    break;
                case "SleepTimer":
                    nbt.putShort("SleepTimer", (short) this.sleepTimer);
                    break;
                case "XpP":
                    nbt.putFloat("XpP", entity.experienceProgress);
                    break;
                case "XpLevel":
                    nbt.putInt("XpLevel", entity.experienceLevel);
                    break;
                case "XpTotal":
                    nbt.putInt("XpTotal", entity.totalExperience);
                    break;
                case "XpSeed":
                    nbt.putInt("XpSeed", this.enchantmentTableSeed);
                    break;
                case "Score":
                    nbt.putInt("Score", entity.getScore());
                    break;
                case "foodLevel":
                case "foodTickTimer":
                case "foodSaturationLevel":
                case "foodExhaustionLevel":
                    this.hungerManager.writeNbt(nbt);
                    break;
                case "abilities":
                    this.abilities.writeNbt(nbt);
                    break;
                case "EnderItems":
                    if(this.enderChestInventory instanceof IFilteredNbtList mixin)
                        nbt.put("EnderItems", mixin.writeNbtFiltered(new NbtList(), path.toString()));
                    else
                        nbt.put("EnderItems", this.enderChestInventory.toNbtList());
                    break;
                case "ShoulderEntityLeft":
                    if (!entity.getShoulderEntityLeft().isEmpty()) {
                        nbt.put("ShoulderEntityLeft", entity.getShoulderEntityLeft());
                    }
                    break;
                case "ShoulderEntityRight":
                    if (!entity.getShoulderEntityRight().isEmpty()) {
                        nbt.put("ShoulderEntityRight", entity.getShoulderEntityRight());
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
        PlayerEntity entity = ((PlayerEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Inventory":
                    NbtList nbtList = nbt.getList("Inventory", 10);
                    this.inventory.readNbt(nbtList);
                    break;
                case "SelectedItemSlot":
                    this.inventory.selectedSlot = nbt.getInt("SelectedItemSlot");
                    break;
                case "SleepTimer":
                    this.sleepTimer = nbt.getShort("SleepTimer");
                    break;
                case "XpP":
                    entity.experienceProgress = nbt.getFloat("XpP");
                    break;
                case "XpLevel":
                    entity.experienceLevel = nbt.getInt("XpLevel");
                    break;
                case "XpTotal":
                    entity.totalExperience = nbt.getInt("XpTotal");
                    break;
                case "XpSeed":
                    this.enchantmentTableSeed = nbt.getInt("XpSeed");
                    if (this.enchantmentTableSeed == 0) {
                        this.enchantmentTableSeed = this.random.nextInt();
                    }
                    break;
                case "Score":
                    entity.setScore(nbt.getInt("Score"));
                    break;
                case "foodLevel":
                case "foodTickTimer":
                case "foodSaturationLevel":
                case "foodExhaustionLevel":
                    this.hungerManager.readNbt(nbt);
                    break;
                case "abilities":
                    this.abilities.readNbt(nbt);
                    break;
                case "EnderItems":
                    if (nbt.contains("EnderItems", 9))
                        this.enderChestInventory.readNbtList(nbt.getList("EnderItems", 10));
                    break;
                case "ShoulderEntityLeft":
                    if (nbt.contains("ShoulderEntityLeft", 10))
                        this.setShoulderEntityLeft(nbt.getCompound("ShoulderEntityLeft"));
                    break;
                case "ShoulderEntityRight":
                    if (nbt.contains("ShoulderEntityRight", 10))
                        this.setShoulderEntityRight(nbt.getCompound("ShoulderEntityRight"));
                    break;
                default:
                    return false;
            }



        }
        return true;
    }
}
