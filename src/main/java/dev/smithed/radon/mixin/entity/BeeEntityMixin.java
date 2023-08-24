package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Shadow int ticksSincePollination;
    @Shadow int cannotEnterHiveTicks;
    @Shadow int cropsGrownSincePollination;
    @Shadow BlockPos hivePos;
    @Shadow BlockPos flowerPos;
    @Shadow abstract void setHasNectar(boolean hasNectar);
    @Shadow abstract void setHasStung(boolean hasStung);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        BeeEntity entity = ((BeeEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "HivePos" -> {
                    if (entity.hasHive())
                        nbt.put("HivePos", NbtHelper.fromBlockPos(entity.getHivePos()));
                }
                case "FlowerPos" -> {
                    if (entity.hasFlower())
                        nbt.put("FlowerPos", NbtHelper.fromBlockPos(entity.getFlowerPos()));
                }
                case "HasNectar" -> nbt.putBoolean("HasNectar", entity.hasNectar());
                case "HasStung" -> nbt.putBoolean("HasStung", entity.hasStung());
                case "TicksSincePollination" -> nbt.putInt("TicksSincePollination", this.ticksSincePollination);
                case "CannotEnterHiveTicks" -> nbt.putInt("CannotEnterHiveTicks", this.cannotEnterHiveTicks);
                case "CropsGrownSincePollination" ->
                        nbt.putInt("CropsGrownSincePollination", this.cropsGrownSincePollination);
                case "AngerTime", "AngerAt" -> entity.writeAngerToNbt(nbt);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        BeeEntity entity = ((BeeEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "HivePos" -> {
                    this.hivePos = null;
                    this.hivePos = NbtHelper.toBlockPos(nbt.getCompound("HivePos"));
                }
                case "FlowerPos" -> {
                    this.flowerPos = null;
                    this.flowerPos = NbtHelper.toBlockPos(nbt.getCompound("FlowerPos"));
                }
                case "HasNectar" -> this.setHasNectar(nbt.getBoolean("HasNectar"));
                case "HasStung" -> this.setHasStung(nbt.getBoolean("HasStung"));
                case "TicksSincePollination" -> this.ticksSincePollination = nbt.getInt("TicksSincePollination");
                case "CannotEnterHiveTicks" -> this.cannotEnterHiveTicks = nbt.getInt("CannotEnterHiveTicks");
                case "CropsGrownSincePollination" ->
                        this.cropsGrownSincePollination = nbt.getInt("CropsGrownSincePollination");
                case "AngerTime", "AngryAt" -> entity.readAngerFromNbt(this.world, nbt);
                default -> {
                    return false;
                }
            }

        }
        return true;
    }
}
