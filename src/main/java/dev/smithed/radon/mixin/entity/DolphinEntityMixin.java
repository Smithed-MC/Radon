package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DolphinEntity.class)
public abstract class DolphinEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        DolphinEntity entity = ((DolphinEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "TreasurePosX" -> nbt.putInt("TreasurePosX", entity.getTreasurePos().getX());
                case "TreasurePosY" -> nbt.putInt("TreasurePosY", entity.getTreasurePos().getY());
                case "TreasurePosZ" -> nbt.putInt("TreasurePosZ", entity.getTreasurePos().getZ());
                case "GotFish" -> nbt.putBoolean("GotFish", entity.hasFish());
                case "Moistness" -> nbt.putInt("Moistness", entity.getMoistness());
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        DolphinEntity entity = ((DolphinEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "TreasurePosX" -> {
                    int i = nbt.getInt("TreasurePosX");
                    entity.setTreasurePos(new BlockPos(i, entity.getTreasurePos().getY(), entity.getTreasurePos().getZ()));
                }
                case "TreasurePosY" -> {
                    int j = nbt.getInt("TreasurePosY");
                    entity.setTreasurePos(new BlockPos(entity.getTreasurePos().getX(), j, entity.getTreasurePos().getZ()));
                }
                case "TreasurePosZ" -> {
                    int k = nbt.getInt("TreasurePosZ");
                    entity.setTreasurePos(new BlockPos(entity.getTreasurePos().getX(), entity.getTreasurePos().getY(), k));
                }
                case "GotFish" -> entity.setHasFish(nbt.getBoolean("GotFish"));
                case "Moistness" -> entity.setMoistness(nbt.getInt("Moistness"));
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
