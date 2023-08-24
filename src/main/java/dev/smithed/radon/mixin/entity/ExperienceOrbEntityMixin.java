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
                case "Health" -> nbt.putShort("Health", (short) this.health);
                case "Age" -> nbt.putShort("Age", (short) this.orbAge);
                case "Value" -> nbt.putShort("Value", (short) this.amount);
                case "Count" -> nbt.putInt("Count", this.pickingCount);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ExperienceOrbEntity entity = ((ExperienceOrbEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "Health" -> this.health = nbt.getShort("Health");
                case "Age" -> this.orbAge = nbt.getShort("Age");
                case "Value" -> this.amount = nbt.getShort("Value");
                case "Count" -> this.pickingCount = Math.max(nbt.getInt("Count"), 1);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
