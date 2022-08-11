package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PassiveEntity.class)
public abstract class PassiveEntityMixin extends MobEntityMixin implements ICustomNBTMixin {
    @Shadow
    protected int forcedAge;
    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, NbtPathArgumentType.NbtPath path, String topLevelNbt) {
        PassiveEntity entity = ((PassiveEntity)(Object)this);
        if(!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Age":
                    nbt.putInt("Age", entity.getBreedingAge());
                    break;
                case "ForcedAge":
                    nbt.putInt("ForcedAge", this.forcedAge);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
