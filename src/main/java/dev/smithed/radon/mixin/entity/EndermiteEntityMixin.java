package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EndermiteEntity.class)
public abstract class EndermiteEntityMixin extends MobEntityMixin implements ICustomNBTMixin {
    @Shadow
    private int lifeTime;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        EndermiteEntity entity = ((EndermiteEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Lifetime":
                    nbt.putInt("Lifetime", this.lifeTime);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
