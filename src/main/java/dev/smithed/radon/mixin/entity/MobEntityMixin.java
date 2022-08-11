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
    @Shadow
    private boolean persistent;
    @Shadow
    private DefaultedList<ItemStack> armorItems;
    @Shadow
    private DefaultedList<ItemStack> handItems;
    @Shadow
    protected float[] handDropChances;
    @Shadow
    protected float[] armorDropChances;
    @Shadow
    private Entity holdingEntity;
    @Shadow
    private NbtCompound leashNbt;
    @Shadow
    private Identifier lootTable;
    @Shadow
    private long lootTableSeed;
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
}
