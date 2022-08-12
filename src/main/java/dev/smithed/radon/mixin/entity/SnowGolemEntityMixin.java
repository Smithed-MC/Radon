package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SnowGolemEntity.class)
public abstract class SnowGolemEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        SnowGolemEntity entity = ((SnowGolemEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Pumpkin":
                    nbt.putBoolean("Pumpkin", entity.hasPumpkin());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
