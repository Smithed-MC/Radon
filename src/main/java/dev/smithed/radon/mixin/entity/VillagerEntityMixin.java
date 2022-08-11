package dev.smithed.radon.mixin.entity;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.village.VillagerGossips;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntityMixin {
    @Shadow
    private int foodLevel;
    @Shadow
    private VillagerGossips gossip;
    @Shadow
    private long lastRestockTime;
    @Shadow
    private int restocksToday;
    @Shadow
    private int experience;
    @Shadow
    private boolean natural;
    @Shadow
    private long lastGossipDecayTime;
    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        VillagerEntity entity = ((VillagerEntity)(Object)this);
        if(!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "FoodLevel":
                    nbt.putByte("FoodLevel", (byte)this.foodLevel);
                    break;
                case "Gossips":
                    nbt.put("Gossips", (NbtElement)this.gossip.serialize(NbtOps.INSTANCE).getValue());
                    break;
                case "Xp":
                    nbt.putInt("Xp", this.experience);
                    break;
                case "LastRestock":
                    nbt.putLong("LastRestock", this.lastRestockTime);
                    break;
                case "LastGossipDecay":
                    nbt.putLong("LastGossipDecay", this.lastGossipDecayTime);
                    break;
                case "RestocksToday":
                    nbt.putInt("RestocksToday", this.restocksToday);
                    break;
                case "AssignProfessionWhenSpawned":
                    if (this.natural) {
                        nbt.putBoolean("AssignProfessionWhenSpawned", true);
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
