package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PaintingEntity.class)
public abstract class PaintingEntityMixin extends AbstractDecorationEntityMixin implements ICustomNBTMixin {

    @Shadow @Final static RegistryKey<PaintingVariant> DEFAULT_VARIANT;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PaintingEntity entity = ((PaintingEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "variant" ->
                        nbt.putString("variant", (entity.getVariant().getKey().orElse(DEFAULT_VARIANT)).getValue().toString());
                case "facing" -> nbt.putByte("facing", (byte) this.facing.getHorizontal());
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PaintingEntity entity = ((PaintingEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            if (topLevelNbt.equals("facing")) {
                this.facing = Direction.fromHorizontal(nbt.getByte("facing"));
                this.setFacing(this.facing);
            } else {
                return false;
            }
        }
        return true;
    }
}
