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
            if (topLevelNbt.equals("Saddle")) {
                this.saddledComponent.writeNbt(nbt);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        StriderEntity entity = ((StriderEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            if (topLevelNbt.equals("Saddle")) {
                this.saddledComponent.readNbt(nbt);
            }
            return false;
        }
        return true;
    }
}
