package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GoatEntity.class)
public abstract class GoatEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Shadow @Final static TrackedData<Boolean> LEFT_HORN;
    @Shadow @Final static TrackedData<Boolean> RIGHT_HORN;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        GoatEntity entity = ((GoatEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "IsScreamingGoat" -> nbt.putBoolean("IsScreamingGoat", entity.isScreaming());
                case "HasLeftHorn" -> nbt.putBoolean("HasLeftHorn", entity.hasLeftHorn());
                case "HasRightHorn" -> nbt.putBoolean("HasRightHorn", entity.hasRightHorn());
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        GoatEntity entity = ((GoatEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "IsScreamingGoat" -> entity.setScreaming(nbt.getBoolean("IsScreamingGoat"));
                case "HasLeftHorn" -> this.dataTracker.set(LEFT_HORN, nbt.getBoolean("HasLeftHorn"));
                case "HasRightHorn" -> this.dataTracker.set(RIGHT_HORN, nbt.getBoolean("HasRightHorn"));
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
