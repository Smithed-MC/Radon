package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin extends EntityMixin implements ICustomNBTMixin {
    @Shadow int orbAge;
    @Shadow int health;
    @Shadow int amount;
    @Shadow int pickingCount;

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

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ExperienceOrbEntity entity = ((ExperienceOrbEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Health":
                    this.health = nbt.getShort("Health");
                    break;
                case "Age":
                    this.orbAge = nbt.getShort("Age");
                    break;
                case "Value":
                    this.amount = nbt.getShort("Value");
                    break;
                case "Count":
                    this.pickingCount = Math.max(nbt.getInt("Count"), 1);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
