package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.SaddledComponent;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StriderEntity.class)
public abstract class StriderEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Shadow @Final SaddledComponent saddledComponent;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        StriderEntity entity = ((StriderEntity) (Object) this);
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

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        StriderEntity entity = ((StriderEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Saddle":
                    this.saddledComponent.readNbt(nbt);
                default:
                    return false;
            }
        }
        return true;
    }
}
