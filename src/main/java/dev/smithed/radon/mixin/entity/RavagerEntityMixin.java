package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RavagerEntity.class)
public abstract class RavagerEntityMixin extends RaiderEntityMixin implements ICustomNBTMixin {

    @Shadow int attackTick;
    @Shadow int stunTick;
    @Shadow int roarTick;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        RavagerEntity entity = ((RavagerEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "AttackTick":
                    nbt.putInt("AttackTick", this.attackTick);
                    break;
                case "StunTick":
                    nbt.putInt("StunTick", this.stunTick);
                    break;
                case "RoarTick":
                    nbt.putInt("RoarTick", this.roarTick);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        RavagerEntity entity = ((RavagerEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "AttackTick":
                    this.attackTick = nbt.getInt("AttackTick");
                    break;
                case "StunTick":
                    this.stunTick = nbt.getInt("StunTick");
                    break;
                case "RoarTick":
                    this.roarTick = nbt.getInt("RoarTick");
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
