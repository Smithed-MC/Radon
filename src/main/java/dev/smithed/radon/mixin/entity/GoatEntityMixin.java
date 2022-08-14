package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GoatEntity.class)
public abstract class GoatEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        GoatEntity entity = ((GoatEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "IsScreamingGoat":
                    nbt.putBoolean("IsScreamingGoat", entity.isScreaming());
                    break;
                case "HasLeftHorn":
                    nbt.putBoolean("HasLeftHorn", entity.hasLeftHorn());
                    break;
                case "HasRightHorn":
                    nbt.putBoolean("HasRightHorn", entity.hasRightHorn());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
