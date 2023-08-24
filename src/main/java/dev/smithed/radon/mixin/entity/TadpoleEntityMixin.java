package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.TadpoleEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TadpoleEntity.class)
public abstract class TadpoleEntityMixin extends FishEntityMixin implements ICustomNBTMixin {

    @Shadow int tadpoleAge;
    @Shadow abstract void setTadpoleAge(int tadpoleAge);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TadpoleEntity entity = ((TadpoleEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("Age")) {
                nbt.putInt("Age", this.tadpoleAge);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TadpoleEntity entity = ((TadpoleEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            if (topLevelNbt.equals("Age")) {
                this.setTadpoleAge(nbt.getInt("Age"));
            } else {
                return false;
            }
        }
        return true;
    }
}
