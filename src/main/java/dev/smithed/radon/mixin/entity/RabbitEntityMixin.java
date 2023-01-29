package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RabbitEntity.class)
public abstract class RabbitEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Shadow int moreCarrotTicks;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        RabbitEntity entity = ((RabbitEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                //TODO: access private id field
                //case "RabbitType":
                //    nbt.putInt("RabbitType", this.getVariant().id);
                //    break;
                case "MoreCarrotTicks":
                    nbt.putInt("MoreCarrotTicks", this.moreCarrotTicks);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        RabbitEntity entity = ((RabbitEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "RabbitType":
                    entity.setVariant(RabbitEntity.RabbitType.byId(nbt.getInt("RabbitType")));
                    break;
                case "MoreCarrotTicks":
                    this.moreCarrotTicks = nbt.getInt("MoreCarrotTicks");
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
