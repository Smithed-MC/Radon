package dev.smithed.radon.mixin.entity;

import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.village.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RaiderEntity.class)
public abstract class RaiderEntityMixin extends PatrolEntityMixin {
    @Shadow
    protected Raid raid;
    @Shadow
    private int wave;
    @Shadow
    private boolean ableToJoinRaid;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        RaiderEntity entity = ((RaiderEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Wave":
                    nbt.putInt("Wave", this.wave);
                    break;
                case "CanJoinRaid":
                    nbt.putBoolean("CanJoinRaid", this.ableToJoinRaid);
                    break;
                case "RaidId":
                    if (this.raid != null)
                        nbt.putInt("RaidId", this.raid.getRaidId());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
