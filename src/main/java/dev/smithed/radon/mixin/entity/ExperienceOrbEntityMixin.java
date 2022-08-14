package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin extends EntityMixin implements ICustomNBTMixin {
    @Shadow
    private int orbAge;
    @Shadow
    private int health;
    @Shadow
    private int amount;
    @Shadow
    private int pickingCount;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ExperienceOrbEntity entity = ((ExperienceOrbEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Health":
                    nbt.putShort("Health", (short)this.health);
                    break;
                case "Age":
                    nbt.putShort("Age", (short)this.orbAge);
                    break;
                case "Value":
                    nbt.putShort("Value", (short)this.amount);
                    break;
                case "Count":
                    nbt.putInt("Count", this.pickingCount);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
