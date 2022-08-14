package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin extends MobEntityMixin implements ICustomNBTMixin {
    @Final
    @Shadow
    private PhaseManager phaseManager;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        EnderDragonEntity entity = ((EnderDragonEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "DragonPhase":
                    nbt.putInt("DragonPhase", this.phaseManager.getCurrent().getType().getTypeId());
                    break;
                case "DragonDeathTime":
                    nbt.putInt("DragonDeathTime", entity.ticksSinceDeath);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
