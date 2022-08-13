package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(EvokerFangsEntity.class)
public abstract class EvokerFangsEntityMixin extends EntityMixin implements ICustomNBTMixin {
    @Shadow
    private int warmup;
    @Shadow
    private UUID ownerUuid;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        EvokerFangsEntity entity = ((EvokerFangsEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Warmup":
                    nbt.putInt("Warmup", this.warmup);
                    break;
                case "Owner":
                    if (this.ownerUuid != null)
                        nbt.putUuid("Owner", this.ownerUuid);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
