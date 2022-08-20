package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GlowSquidEntity.class)
public abstract class GlowSquidEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Shadow abstract void setDarkTicksRemaining(int ticks);

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

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        GlowSquidEntity entity = ((GlowSquidEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "DarkTicksRemaining":
                    this.setDarkTicksRemaining(nbt.getInt("DarkTicksRemaining"));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
