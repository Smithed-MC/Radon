package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Mixin(FoxEntity.class)
public abstract class FoxEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Shadow abstract List<UUID> getTrustedUuids();
    @Shadow abstract void addTrustedUuid(@Nullable UUID uuid);
    @Shadow abstract void setSleeping(boolean sleeping);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FoxEntity entity = ((FoxEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Trusted" -> {
                    List<UUID> list = this.getTrustedUuids();
                    NbtList nbtList = new NbtList();
                    for (UUID uUID : list) {
                        if (uUID != null) {
                            nbtList.add(NbtHelper.fromUuid(uUID));
                        }
                    }
                    nbt.put("Trusted", nbtList);
                }
                case "AngryAt" -> nbt.putBoolean("Sleeping", entity.isSleeping());
                case "Type" -> nbt.putString("Type", entity.getVariant().asString());
                case "Sitting" -> nbt.putBoolean("Sitting", entity.isSitting());
                case "Crouching" -> nbt.putBoolean("Crouching", entity.isInSneakingPose());
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FoxEntity entity = ((FoxEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "Trusted" -> {
                    NbtList nbtList = nbt.getList("Trusted", 11);
                    for (net.minecraft.nbt.NbtElement nbtElement : nbtList) {
                        this.addTrustedUuid(NbtHelper.toUuid(nbtElement));
                    }
                }
                case "Sleeping" -> this.setSleeping(nbt.getBoolean("Sleeping"));
                case "Type" -> entity.setVariant(FoxEntity.Type.byName(nbt.getString("Type")));
                case "Sitting" -> entity.setSitting(nbt.getBoolean("Sitting"));
                case "Crouching" -> entity.setCrouching(nbt.getBoolean("Crouching"));
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

}
