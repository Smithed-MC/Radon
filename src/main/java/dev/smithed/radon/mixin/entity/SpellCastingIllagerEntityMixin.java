package dev.smithed.radon.mixin.entity;

import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SpellcastingIllagerEntity.class)
public abstract class SpellCastingIllagerEntityMixin extends RaiderEntityMixin {

    @Shadow int spellTicks;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        SpellcastingIllagerEntity entity = ((SpellcastingIllagerEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("SpellTicks")) {
                nbt.putInt("SpellTicks", this.spellTicks);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        SpellcastingIllagerEntity entity = ((SpellcastingIllagerEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            if (topLevelNbt.equals("SpellTicks")) {
                this.spellTicks = nbt.getInt("SpellTicks");
            } else {
                return false;
            }
        }
        return true;
    }
}
