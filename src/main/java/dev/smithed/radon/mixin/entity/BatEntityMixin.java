package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BatEntity.class)
public abstract class BatEntityMixin extends MobEntityMixin implements ICustomNBTMixin {
    @Final
    @Shadow
    private static TrackedData<Byte> BAT_FLAGS;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        BatEntity entity = ((BatEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "BatFlags":
                    nbt.putByte("BatFlags", (Byte)this.dataTracker.get(BAT_FLAGS));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
