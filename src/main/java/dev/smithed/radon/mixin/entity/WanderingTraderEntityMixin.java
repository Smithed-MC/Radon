package dev.smithed.radon.mixin.entity;

import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin extends MerchantEntityMixin {

    @Shadow BlockPos wanderTarget;
    @Shadow int despawnDelay;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        WanderingTraderEntity entity = ((WanderingTraderEntity)(Object)this);
        if(!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "DespawnDelay" -> nbt.putInt("DespawnDelay", this.despawnDelay);
                case "WanderTarget" -> {
                    if (this.wanderTarget != null) {
                        nbt.put("WanderTarget", NbtHelper.fromBlockPos(this.wanderTarget));
                    }
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        WanderingTraderEntity entity = ((WanderingTraderEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "DespawnDelay" -> {
                    if (nbt.contains("DespawnDelay", 99))
                        this.despawnDelay = nbt.getInt("DespawnDelay");
                }
                case "WanderTarget" -> {
                    if (nbt.contains("WanderTarget"))
                        this.wanderTarget = NbtHelper.toBlockPos(nbt.getCompound("WanderTarget"));
                }
                default -> {
                    return false;
                }
            }
        }
        entity.setBreedingAge(Math.max(0, entity.getBreedingAge()));
        return true;
    }
}
