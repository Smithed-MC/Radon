package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ZombifiedPiglinEntity.class)
public abstract class ZombifiedPiglinEntityMixin extends ZombieEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ZombifiedPiglinEntity entity = ((ZombifiedPiglinEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "AngerTime":
                    nbt.putInt("AngerTime", entity.getAngerTime());
                    break;
                case "AngryAt":
                    if (entity.getAngryAt() != null)
                        nbt.putUuid("AngryAt", entity.getAngryAt());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
