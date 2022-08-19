package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChickenEntity.class)
public abstract class ChickenEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ChickenEntity entity = ((ChickenEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "AngerTime":
                    nbt.putBoolean("IsChickenJockey", entity.hasJockey);
                    break;
                case "AngryAt":
                    nbt.putInt("EggLayTime", entity.eggLayTime);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ChickenEntity entity = ((ChickenEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "IsChickenJockey":
                    entity.hasJockey = nbt.getBoolean("IsChickenJockey");
                    break;
                case "ChestedHorse":
                    entity.eggLayTime = nbt.getInt("EggLayTime");
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
