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
            if (topLevelNbt.equals("Trusting")) {
                nbt.putBoolean("Trusting", this.isTrusting());
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        OcelotEntity entity = ((OcelotEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            if (topLevelNbt.equals("Trusting")) {
                this.setTrusting(nbt.getBoolean("Trusting"));
            } else {
                return false;
            }
        }
        return true;
    }
}
