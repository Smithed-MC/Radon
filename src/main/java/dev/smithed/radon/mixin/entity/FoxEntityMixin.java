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
                    nbt.putString("Type", entity.getVariant().asString());
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

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FoxEntity entity = ((FoxEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Trusted":
                    NbtList nbtList = nbt.getList("Trusted", 11);
                    for(int i = 0; i < nbtList.size(); ++i) {
                        this.addTrustedUuid(NbtHelper.toUuid(nbtList.get(i)));
                    }
                    break;
                case "Sleeping":
                    this.setSleeping(nbt.getBoolean("Sleeping"));
                    break;
                case "Type":
                    entity.setVariant(FoxEntity.Type.byName(nbt.getString("Type")));
                    break;
                case "Sitting":
                    entity.setSitting(nbt.getBoolean("Sitting"));
                    break;
                case "Crouching":
                    entity.setCrouching(nbt.getBoolean("Crouching"));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

}
