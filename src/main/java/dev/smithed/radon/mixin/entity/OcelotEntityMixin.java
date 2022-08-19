package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OcelotEntity.class)
public abstract class OcelotEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Shadow abstract boolean isTrusting();
    @Shadow abstract void setTrusting(boolean trusting);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        OcelotEntity entity = ((OcelotEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Trusting":
                    nbt.putBoolean("Trusting", this.isTrusting());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        OcelotEntity entity = ((OcelotEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Trusting":
                    this.setTrusting(nbt.getBoolean("Trusting"));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
