package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Shadow int inWaterTime;
    @Shadow int ticksUntilWaterConversion;
    @Shadow abstract void setTicksUntilWaterConversion(int ticksUntilWaterConversion);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
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

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ZombieEntity entity = ((ZombieEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "IsBaby":
                    entity.setBaby(nbt.getBoolean("IsBaby"));
                    break;
                case "CanBreakDoors":
                    entity.setCanBreakDoors(nbt.getBoolean("CanBreakDoors"));
                    break;
                case "InWaterTime":
                    this.inWaterTime = nbt.getInt("InWaterTime");
                    break;
                case "DrownedConversionTime":
                    if (nbt.contains("DrownedConversionTime", 99) && nbt.getInt("DrownedConversionTime") > -1)
                        this.setTicksUntilWaterConversion(nbt.getInt("DrownedConversionTime"));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

}
