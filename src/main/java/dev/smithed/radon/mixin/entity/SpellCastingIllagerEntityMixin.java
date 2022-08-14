package dev.smithed.radon.mixin.entity;

import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SpellcastingIllagerEntity.class)
public abstract class SpellCastingIllagerEntityMixin extends RaiderEntityMixin {
    @Shadow
    protected int spellTicks;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        SpellcastingIllagerEntity entity = ((SpellcastingIllagerEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "SpellTicks":
                    nbt.putInt("SpellTicks", this.spellTicks);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
