package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IronGolemEntity.class)
public abstract class IronGolemEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        IronGolemEntity entity = ((IronGolemEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "AngerTime":
                    nbt.putInt("AngerTime", entity.getAngerTime());
                    break;
                case "AngryAt":
                    if (entity.getAngryAt() != null)
                        nbt.putUuid("AngryAt", entity.getAngryAt());
                    break;
                case "PlayerCreated":
                    nbt.putBoolean("PlayerCreated", entity.isPlayerCreated());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
