package dev.smithed.radon.mixin.entity;

import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RaiderEntity.class)
public abstract class RaiderEntityMixin extends PatrolEntityMixin {

    @Shadow Raid raid;
    @Shadow int wave;
    @Shadow boolean ableToJoinRaid;

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

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        RaiderEntity entity = ((RaiderEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Wave":
                    this.wave = nbt.getInt("Wave");
                    break;
                case "CanJoinRaid":
                    this.ableToJoinRaid = nbt.getBoolean("CanJoinRaid");
                    break;
                case "RaidId":
                    if (nbt.contains("RaidId", 3)) {
                        if (this.world instanceof ServerWorld) {
                            this.raid = ((ServerWorld)this.world).getRaidManager().getRaid(nbt.getInt("RaidId"));
                        }

                        if (this.raid != null) {
                            this.raid.addToWave(this.wave, entity, false);
                            if (entity.isPatrolLeader()) {
                                this.raid.setWaveCaptain(this.wave, entity);
                            }
                        }
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
