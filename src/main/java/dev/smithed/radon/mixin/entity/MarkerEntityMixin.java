package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MarkerEntity.class)
public abstract class MarkerEntityMixin extends EntityMixin implements ICustomNBTMixin {
    @Shadow
    private NbtCompound data;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        MarkerEntity entity = ((MarkerEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "data":
                    nbt.put("data", this.data.copy());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
