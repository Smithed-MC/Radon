package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PufferfishEntity.class)
public abstract class PufferfishEntityMixin extends FishEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PufferfishEntity entity = ((PufferfishEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("PuffState")) {
                nbt.putInt("PuffState", entity.getPuffState());
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PufferfishEntity entity = ((PufferfishEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            if (topLevelNbt.equals("PuffState")) {
                entity.setPuffState(Math.min(nbt.getInt("PuffState"), 2));
            } else {
                return false;
            }
        }
        return true;
    }
}
