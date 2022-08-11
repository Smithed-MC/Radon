package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends MobEntityMixin implements ICustomNBTMixin {
    @Shadow
    private int inWaterTime;
    @Shadow
    private int ticksUntilWaterConversion;
    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, NbtPathArgumentType.NbtPath path, String topLevelNbt) {
        ZombieEntity entity = ((ZombieEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "IsBaby":
                    nbt.putBoolean("IsBaby", entity.isBaby());
                    break;
                case "CanBreakDoors":
                    nbt.putBoolean("CanBreakDoors", entity.canBreakDoors());
                    break;
                case "InWaterTime":
                    nbt.putInt("InWaterTime", entity.isTouchingWater() ? this.inWaterTime : -1);
                case "DrownedConversionTime":
                    nbt.putInt("DrownedConversionTime", entity.isConvertingInWater() ? this.ticksUntilWaterConversion : -1);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

}
