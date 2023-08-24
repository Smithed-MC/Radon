package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.ServerConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(TameableEntity.class)
public abstract class TameableEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {

    @Shadow boolean sitting;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TameableEntity entity = ((TameableEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Sitting" -> nbt.putBoolean("Sitting", this.sitting);
                case "Owner" -> {
                    if (entity.getOwnerUuid() != null)
                        nbt.putUuid("Owner", entity.getOwnerUuid());
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TameableEntity entity = ((TameableEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "Owner":
                    UUID uUID;
                    if (nbt.containsUuid("Owner")) {
                        uUID = nbt.getUuid("Owner");
                    } else {
                        String string = nbt.getString("Owner");
                        uUID = ServerConfigHandler.getPlayerUuidByName(entity.getServer(), string);
                    }
                    if (uUID != null) {
                        try {
                            entity.setOwnerUuid(uUID);
                            entity.setTamed(true);
                        } catch (Throwable var4) {
                            entity.setTamed(false);
                        }
                    }
                    break;
                case "Sitting":
                    this.sitting = nbt.getBoolean("Sitting");
                    entity.setInSittingPose(this.sitting);
                default:
                    return false;
            }
        }
        return true;
    }
}
