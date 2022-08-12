package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.TadpoleEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TadpoleEntity.class)
public abstract class TadpoleEntityMixin extends FishEntityMixin implements ICustomNBTMixin {
    @Shadow
    private int tadpoleAge;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TadpoleEntity entity = ((TadpoleEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Age":
                    nbt.putInt("Age", this.tadpoleAge);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
