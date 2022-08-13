package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RabbitEntity.class)
public abstract class RabbitEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {
    @Shadow
    int moreCarrotTicks;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        RabbitEntity entity = ((RabbitEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "RabbitType":
                    nbt.putInt("RabbitType", entity.getRabbitType());
                    break;
                case "MoreCarrotTicks":
                    nbt.putInt("MoreCarrotTicks", this.moreCarrotTicks);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
