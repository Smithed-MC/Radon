package dev.smithed.radon.mixin.entity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.village.VillagerData;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;
import java.util.UUID;

@Mixin(ZombieVillagerEntity.class)
public abstract class ZombieVillagerEntityMixin extends ZombieEntityMixin implements ICustomNBTMixin {

    @Shadow @Final static Logger field_36334 = LogUtils.getLogger();
    @Shadow int conversionTimer;
    @Shadow UUID converter;
    @Shadow NbtElement gossipData;
    @Shadow NbtCompound offerData;
    @Shadow int xp;
    @Shadow abstract void setConverting(@Nullable UUID uuid, int delay);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ZombieVillagerEntity entity = ((ZombieVillagerEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Gossips":
                    if (this.gossipData != null) {
                        nbt.put("Gossips", this.gossipData);
                    }
                    break;
                case "Offers":
                    if (this.offerData != null) {
                        nbt.put("Offers", this.offerData);
                    }
                    break;
                case "ConversionTime":
                    nbt.putInt("ConversionTime", entity.isConverting() ? this.conversionTimer : -1);
                case "ConversionPlayer":
                    if (this.converter != null) {
                        nbt.putUuid("ConversionPlayer", this.converter);
                    }
                    break;
                case "Xp":
                    if (this.converter != null) {
                        nbt.putInt("Xp", this.xp);
                    }
                    break;
                case "VillagerData":
                    DataResult<NbtElement> var10000 = VillagerData.CODEC.encodeStart(NbtOps.INSTANCE, entity.getVillagerData());
                    Logger var10001 = field_36334;
                    Objects.requireNonNull(var10001);
                    var10000.resultOrPartial(var10001::error).ifPresent((nbtElement) -> {
                        nbt.put("VillagerData", nbtElement);
                    });
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ZombieVillagerEntity entity = ((ZombieVillagerEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Offers":
                    if (nbt.contains("Offers", 10))
                        this.offerData = nbt.getCompound("Offers");
                    break;
                case "Gossips":
                    if (nbt.contains("Gossips", 10))
                        this.gossipData = nbt.getList("Gossips", 10);
                    break;
                case "ConversionTime":
                    if (nbt.contains("ConversionTime", 99) && nbt.getInt("ConversionTime") > -1)
                        this.setConverting(nbt.containsUuid("ConversionPlayer") ? nbt.getUuid("ConversionPlayer") : null, nbt.getInt("ConversionTime"));
                    break;
                case "Xp":
                    if (nbt.contains("Xp", 3))
                        this.xp = nbt.getInt("Xp");
                    break;
                case "VillagerData":
                    if (nbt.contains("VillagerData", 10)) {
                        DataResult<VillagerData> dataResult = VillagerData.CODEC.parse(new Dynamic(NbtOps.INSTANCE, nbt.get("VillagerData")));
                        Logger var10001 = field_36334;
                        Objects.requireNonNull(var10001);
                        dataResult.resultOrPartial(var10001::error).ifPresent(entity::setVillagerData);
                    }
                    break;
                default:
                    return false;
            }




        }
        return true;
    }
}
