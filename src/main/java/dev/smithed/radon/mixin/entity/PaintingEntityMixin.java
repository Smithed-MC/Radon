package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PaintingEntity.class)
public abstract class PaintingEntityMixin extends AbstractDecorationEntityMixin implements ICustomNBTMixin {
    @Final
    @Shadow
    private static RegistryKey<PaintingVariant> DEFAULT_VARIANT;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PaintingEntity entity = ((PaintingEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "variant":
                    nbt.putString("variant", ((RegistryKey)entity.getVariant().getKey().orElse(DEFAULT_VARIANT)).getValue().toString());
                    break;
                case "facing":
                    nbt.putByte("facing", (byte)this.facing.getHorizontal());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
