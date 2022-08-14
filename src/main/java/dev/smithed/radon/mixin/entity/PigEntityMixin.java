package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.SaddledComponent;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PigEntity.class)
public abstract class PigEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {
    @Final
    @Shadow
    private SaddledComponent saddledComponent;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PigEntity entity = ((PigEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Saddle":
                    this.saddledComponent.writeNbt(nbt);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
