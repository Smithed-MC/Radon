package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GlowSquidEntity.class)
public abstract class GlowSquidEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        GlowSquidEntity entity = ((GlowSquidEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "DarkTicksRemaining":
                    nbt.putInt("DarkTicksRemaining", entity.getDarkTicksRemaining());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
