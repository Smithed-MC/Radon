package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShulkerEntity.class)
public abstract class ShulkerEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Shadow @Final static TrackedData<Byte> PEEK_AMOUNT;
    @Shadow @Final static TrackedData<Byte> COLOR;
    @Shadow abstract void setAttachedFace(Direction face);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ShulkerEntity entity = ((ShulkerEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "AttachFace" -> nbt.putByte("AttachFace", (byte) entity.getAttachedFace().getId());
                case "Peek" -> nbt.putByte("Peek", (Byte) this.dataTracker.get(PEEK_AMOUNT));
                case "Color" -> nbt.putByte("Color", (Byte) this.dataTracker.get(COLOR));
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ShulkerEntity entity = ((ShulkerEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "AttachFace" -> this.setAttachedFace(Direction.byId(nbt.getByte("AttachFace")));
                case "Peek" -> this.dataTracker.set(PEEK_AMOUNT, nbt.getByte("Peek"));
                case "Color" -> {
                    if (nbt.contains("Color", 99))
                        this.dataTracker.set(COLOR, nbt.getByte("Color"));
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
