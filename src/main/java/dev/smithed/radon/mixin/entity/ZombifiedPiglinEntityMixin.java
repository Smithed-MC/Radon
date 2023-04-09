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
                case "AngerTime" -> nbt.putInt("AngerTime", entity.getAngerTime());
                case "AngryAt" -> {
                    if (entity.getAngryAt() != null)
                        nbt.putUuid("AngryAt", entity.getAngryAt());
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ZombifiedPiglinEntity entity = ((ZombifiedPiglinEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "AngerAt", "AngerTime" -> entity.readAngerFromNbt(this.world, nbt);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
