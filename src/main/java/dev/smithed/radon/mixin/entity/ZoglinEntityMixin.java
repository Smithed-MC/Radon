package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.ZoglinEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ZoglinEntity.class)
public abstract class ZoglinEntityMixin extends MobEntityMixin implements ICustomNBTMixin  {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ZoglinEntity entity = ((ZoglinEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "IsBaby":
                    if (entity.isBaby())
                        nbt.putBoolean("IsBaby", true);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
