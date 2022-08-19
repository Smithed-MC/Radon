package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.UUID;
@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntityMixin implements ICustomNBTMixin {

    @Shadow boolean persistent;
    @Shadow DefaultedList<ItemStack> armorItems;
    @Shadow DefaultedList<ItemStack> handItems;
    @Shadow float[] handDropChances;
    @Shadow float[] armorDropChances;
    @Shadow Entity holdingEntity;
    @Shadow NbtCompound leashNbt;
    @Shadow Identifier lootTable;
    @Shadow long lootTableSeed;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        MobEntity entity = ((MobEntity)(Object)this);
        if(!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "CanPickUpLoot":
                    nbt.putBoolean("CanPickUpLoot", entity.canPickUpLoot());
                    break;
                case "PersistenceRequired":
                    nbt.putBoolean("PersistenceRequired", this.persistent);
                    break;
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
                    for (Iterator var11 = this.handItems.iterator(); var11.hasNext(); nbtList2.add(nbtCompound2)) {
                        ItemStack itemStack2 = (ItemStack) var11.next();
                        nbtCompound2 = new NbtCompound();
                        if (!itemStack2.isEmpty()) {
                            itemStack2.writeNbt(nbtCompound2);
                        }
                    }
                    nbt.put("HandItems", nbtList2);
                    break;
                case "ArmorDropChances":
                    NbtList nbtList3 = new NbtList();
                    float[] var14 = this.armorDropChances;
                    int var16 = var14.length;
                    int var7;
                    for (var7 = 0; var7 < var16; ++var7) {
                        float f = var14[var7];
                        nbtList3.add(NbtFloat.of(f));
                    }
                    nbt.put("ArmorDropChances", nbtList3);
                    break;
                case "HandDropChances":
                    NbtList nbtList4 = new NbtList();
                    float[] var17 = this.handDropChances;
                    var7 = var17.length;

                    for (int var19 = 0; var19 < var7; ++var19) {
                        float g = var17[var19];
                        nbtList4.add(NbtFloat.of(g));
                    }

                    nbt.put("HandDropChances", nbtList4);
                    break;
                case "Leash":
                    if (this.holdingEntity != null) {
                        nbtCompound2 = new NbtCompound();
                        if (this.holdingEntity instanceof LivingEntity) {
                            UUID uUID = this.holdingEntity.getUuid();
                            nbtCompound2.putUuid("UUID", uUID);
                        } else if (this.holdingEntity instanceof AbstractDecorationEntity) {
                            BlockPos blockPos = ((AbstractDecorationEntity) this.holdingEntity).getDecorationBlockPos();
                            nbtCompound2.putInt("X", blockPos.getX());
                            nbtCompound2.putInt("Y", blockPos.getY());
                            nbtCompound2.putInt("Z", blockPos.getZ());
                        }

                        nbt.put("Leash", nbtCompound2);
                    } else if (this.leashNbt != null) {
                        nbt.put("Leash", this.leashNbt.copy());
                    }
                    break;
                case "LeftHanded":
                    nbt.putBoolean("LeftHanded", entity.isLeftHanded());
                    break;
                case "DeathLootTable":
                    if (this.lootTable != null) {
                        nbt.putString("DeathLootTable", this.lootTable.toString());
                        if (this.lootTableSeed != 0L) {
                            nbt.putLong("DeathLootTableSeed", this.lootTableSeed);
                        }
                    }
                    break;
                case "NoAI":
                    if (entity.isAiDisabled()) {
                        nbt.putBoolean("NoAI", entity.isAiDisabled());
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
        MobEntity entity = ((MobEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            NbtList nbtList;
            switch (topLevelNbt) {
                case "CanPickUpLoot":
                    if (nbt.contains("CanPickUpLoot", 1))
                        entity.setCanPickUpLoot(nbt.getBoolean("CanPickUpLoot"));
                    break;
                case "PersistenceRequired":
                    this.persistent = nbt.getBoolean("PersistenceRequired");
                    break;
                case "ArmorItems":
                    if (nbt.contains("ArmorItems", 9)) {
                        nbtList = nbt.getList("ArmorItems", 10);
                        for(int i = 0; i < this.armorItems.size(); ++i) {
                            this.armorItems.set(i, ItemStack.fromNbt(nbtList.getCompound(i)));
                        }
                    }
                    break;
                case "HandItems":
                    if (nbt.contains("HandItems", 9)) {
                        nbtList = nbt.getList("HandItems", 10);
                        for(int i = 0; i < this.handItems.size(); ++i) {
                            this.handItems.set(i, ItemStack.fromNbt(nbtList.getCompound(i)));
                        }
                    }
                    break;
                case "ArmorDropChances":
                    if (nbt.contains("ArmorDropChances", 9)) {
                        nbtList = nbt.getList("ArmorDropChances", 5);
                        for(int i = 0; i < nbtList.size(); ++i) {
                            this.armorDropChances[i] = nbtList.getFloat(i);
                        }
                    }
                    break;
                case "HandDropChances":
                    if (nbt.contains("HandDropChances", 9)) {
                        nbtList = nbt.getList("HandDropChances", 5);
                        for(int i = 0; i < nbtList.size(); ++i) {
                            this.handDropChances[i] = nbtList.getFloat(i);
                        }
                    }
                    break;
                case "Leash":
                    if (nbt.contains("Leash", 10))
                        this.leashNbt = nbt.getCompound("Leash");
                    break;
                case "LeftHanded":
                    entity.setLeftHanded(nbt.getBoolean("LeftHanded"));
                    break;
                case "Tag":
                    if (nbt.contains("DeathLootTable", 8))
                        this.lootTable = new Identifier(nbt.getString("DeathLootTable"));
                    break;
                case "DeathLootTableSeed":
                    this.lootTableSeed = nbt.getLong("DeathLootTableSeed");
                    break;
                case "NoAI":
                    entity.setAiDisabled(nbt.getBoolean("NoAI"));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
