package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShulkerEntity.class)
public abstract class ShulkerEntityMixin extends MobEntityMixin implements ICustomNBTMixin {
    @Final
    @Shadow
    protected static TrackedData<Byte> PEEK_AMOUNT;
    @Final
    @Shadow
    protected static TrackedData<Byte> COLOR;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ShulkerEntity entity = ((ShulkerEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "AttachFace":
                    nbt.putByte("AttachFace", (byte)entity.getAttachedFace().getId());
                    break;
                case "Peek":
                    nbt.putByte("Peek", (Byte)this.dataTracker.get(PEEK_AMOUNT));
                    break;
                case "Color":
                    nbt.putByte("Color", (Byte)this.dataTracker.get(COLOR));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
