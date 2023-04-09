package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EndermiteEntity.class)
public abstract class EndermiteEntityMixin extends MobEntityMixin implements ICustomNBTMixin {
    @Shadow int lifeTime;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        EndermiteEntity entity = ((EndermiteEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("Lifetime")) {
                nbt.putInt("Lifetime", this.lifeTime);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        EndermiteEntity entity = ((EndermiteEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            if (topLevelNbt.equals("Lifetime")) {
                this.lifeTime = nbt.getInt("Lifetime");
            } else {
                return false;
            }
        }
        return true;
    }
}
