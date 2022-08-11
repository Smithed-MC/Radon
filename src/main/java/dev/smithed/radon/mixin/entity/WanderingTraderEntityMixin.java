package dev.smithed.radon.mixin.entity;

import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin extends MerchantEntityMixin {
    @Shadow
    private BlockPos wanderTarget;
    @Shadow
    private int despawnDelay;
    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, NbtPathArgumentType.NbtPath path, String topLevelNbt) {
        WanderingTraderEntity entity = ((WanderingTraderEntity)(Object)this);
        if(!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "DespawnDelay":
                    nbt.putInt("DespawnDelay", this.despawnDelay);
                    break;
                case "WanderTarget":
                    if (this.wanderTarget != null) {
                        nbt.put("WanderTarget", NbtHelper.fromBlockPos(this.wanderTarget));
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
