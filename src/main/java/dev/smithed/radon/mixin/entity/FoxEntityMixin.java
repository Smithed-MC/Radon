package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Mixin(FoxEntity.class)
public abstract class FoxEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {
    @Shadow
    abstract List<UUID> getTrustedUuids();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FoxEntity entity = ((FoxEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Trusted":
                    List<UUID> list = this.getTrustedUuids();
                    NbtList nbtList = new NbtList();
                    Iterator var4 = list.iterator();

                    while(var4.hasNext()) {
                        UUID uUID = (UUID)var4.next();
                        if (uUID != null) {
                            nbtList.add(NbtHelper.fromUuid(uUID));
                        }
                    }
                    nbt.put("Trusted", nbtList);
                    break;
                case "AngryAt":
                    nbt.putBoolean("Sleeping", entity.isSleeping());
                    break;
                case "Type":
                    nbt.putString("Type", entity.getFoxType().getKey());
                    break;
                case "Sitting":
                    nbt.putBoolean("Sitting", entity.isSitting());
                    break;
                case "Crouching":
                    nbt.putBoolean("Crouching", entity.isInSneakingPose());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
