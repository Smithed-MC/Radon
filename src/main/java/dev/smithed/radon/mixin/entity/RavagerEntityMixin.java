package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RavagerEntity.class)
public abstract class RavagerEntityMixin extends RaiderEntityMixin implements ICustomNBTMixin {
    @Shadow
    private int attackTick;
    @Shadow
    private int stunTick;
    @Shadow
    private int roarTick;

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
}
