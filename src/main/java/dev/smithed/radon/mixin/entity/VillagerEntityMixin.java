package dev.smithed.radon.mixin.entity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import dev.smithed.radon.Radon;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerGossips;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntityMixin {

    @Shadow int foodLevel;
    @Shadow VillagerGossips gossip;
    @Shadow long lastRestockTime;
    @Shadow int restocksToday;
    @Shadow int experience;
    @Shadow boolean natural;
    @Shadow long lastGossipDecayTime;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        VillagerEntity entity = ((VillagerEntity)(Object)this);
        if(!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "FoodLevel":
                    nbt.putByte("FoodLevel", (byte)this.foodLevel);
                    break;
                case "Gossips":
                    nbt.put("Gossips", (NbtElement)this.gossip.serialize(NbtOps.INSTANCE));
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
                case "VillagerData":
                    DataResult<NbtElement> data = VillagerData.CODEC.encodeStart(NbtOps.INSTANCE, entity.getVillagerData());
                    Logger logger = Radon.LOGGER;
                    Objects.requireNonNull(logger);
                    data.resultOrPartial(logger::error).ifPresent(nbtElement -> nbt.put("VillagerData", nbtElement));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        VillagerEntity entity = ((VillagerEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Offers":
                    if (nbt.contains("Offers", 10))
                        this.offers = new TradeOfferList(nbt.getCompound("Offers"));
                    break;
                case "FoodLevel":
                    if (nbt.contains("FoodLevel", 1))
                        this.foodLevel = nbt.getByte("FoodLevel");
                    break;
                case "Gossips":
                    NbtList nbtList = nbt.getList("Gossips", 10);
                    this.gossip.deserialize(new Dynamic(NbtOps.INSTANCE, nbtList));
                    break;
                case "Xp":
                    if (nbt.contains("Xp", 3))
                        this.experience = nbt.getInt("Xp");
                    break;
                case "LastRestock":
                    this.lastRestockTime = nbt.getLong("LastRestock");
                    break;
                case "LastGossipDecay":
                    this.lastGossipDecayTime = nbt.getLong("LastGossipDecay");
                    break;
                case "RestocksToday":
                    this.restocksToday = nbt.getInt("RestocksToday");
                    break;
                case "AssignProfessionWhenSpawned":
                    this.natural = nbt.getBoolean("AssignProfessionWhenSpawned");
                    break;
                case "VillagerData":
                    if (nbt.contains("VillagerData", 10)) {
                        DataResult<VillagerData> dataResult = VillagerData.CODEC.parse(new Dynamic(NbtOps.INSTANCE, nbt.get("VillagerData")));
                        Logger logger = Radon.LOGGER;
                        Objects.requireNonNull(logger);
                        dataResult.resultOrPartial(logger::error).ifPresent(entity::setVillagerData);
                    }
                    break;
                default:
                    return false;
            }
        }
        if (this.world instanceof ServerWorld)
            entity.reinitializeBrain((ServerWorld)this.world);
        entity.setCanPickUpLoot(true);
        return true;
    }
}
