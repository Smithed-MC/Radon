package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HoglinEntity.class)
public abstract class HoglinEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {
    @Shadow int timeInOverworld;
    @Shadow boolean cannotBeHunted;
    @Shadow abstract boolean isImmuneToZombification();
    @Shadow abstract void setCannotBeHunted(boolean cannotBeHunted);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        HoglinEntity entity = ((HoglinEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "AngerTime":
                    if (this.isImmuneToZombification())
                        nbt.putBoolean("IsImmuneToZombification", true);
                    break;
                case "TimeInOverworld":
                    nbt.putInt("TimeInOverworld", this.timeInOverworld);
                    break;
                case "CannotBeHunted":
                    if (this.cannotBeHunted)
                        nbt.putBoolean("CannotBeHunted", true);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        HoglinEntity entity = ((HoglinEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "IsImmuneToZombification":
                    entity.setImmuneToZombification(nbt.getBoolean("IsImmuneToZombification"));
                    break;
                case "TimeInOverworld":
                    this.timeInOverworld = nbt.getInt("TimeInOverworld");
                    break;
                case "CannotBeHunted":
                    this.setCannotBeHunted(nbt.getBoolean("CannotBeHunted"));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
