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
            if (topLevelNbt.equals("DarkTicksRemaining")) {
                nbt.putInt("DarkTicksRemaining", entity.getDarkTicksRemaining());
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        GlowSquidEntity entity = ((GlowSquidEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            if (topLevelNbt.equals("DarkTicksRemaining")) {
                this.setDarkTicksRemaining(nbt.getInt("DarkTicksRemaining"));
            } else {
                return false;
            }
        }
        return true;
    }
}
