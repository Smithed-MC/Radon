package dev.smithed.radon.mixin.entity;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends AnimalEntityMixin {
    @Shadow
    int ticksSincePollination;
    @Shadow
    private int cannotEnterHiveTicks;
    @Shadow
    private int cropsGrownSincePollination;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        BeeEntity entity = ((BeeEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "HivePos":
                    if (entity.hasHive())
                        nbt.put("HivePos", NbtHelper.fromBlockPos(entity.getHivePos()));
                    break;
                case "FlowerPos":
                    if (entity.hasFlower())
                        nbt.put("FlowerPos", NbtHelper.fromBlockPos(entity.getFlowerPos()));
                    break;
                case "HasNectar":
                    nbt.putBoolean("HasNectar", entity.hasNectar());
                    break;
                case "HasStung":
                    nbt.putBoolean("HasStung", entity.hasStung());
                    break;
                case "TicksSincePollination":
                    nbt.putInt("TicksSincePollination", this.ticksSincePollination);
                    break;
                case "CannotEnterHiveTicks":
                    nbt.putInt("CannotEnterHiveTicks", this.cannotEnterHiveTicks);
                    break;
                case "CropsGrownSincePollination":
                    nbt.putInt("CropsGrownSincePollination", this.cropsGrownSincePollination);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
