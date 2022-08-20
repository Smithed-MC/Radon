package dev.smithed.radon.mixin.entity;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.event.listener.VibrationListener;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(AllayEntity.class)
public abstract class AllayEntityMixin extends MobEntityMixin implements ICustomNBTMixin {
    @Shadow @Final SimpleInventory inventory;
    @Shadow @Final static TrackedData<Boolean> CAN_DUPLICATE;
    @Shadow long duplicationCooldown;
    @Shadow abstract boolean canDuplicate();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AllayEntity entity = ((AllayEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Inventory":
                    nbt.put("Inventory", this.inventory.toNbtList());
                    break;
                case "DuplicationCooldown":
                    nbt.putLong("DuplicationCooldown", this.duplicationCooldown);
                    break;
                case "CanDuplicate":
                    nbt.putBoolean("CanDuplicate", this.canDuplicate());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AllayEntity entity = ((AllayEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "CanDuplicate":
                    this.dataTracker.set(CAN_DUPLICATE, nbt.getBoolean("CanDuplicate"));
                    break;
                case "DuplicationCooldown":
                    this.duplicationCooldown = (long)nbt.getInt("DuplicationCooldown");
                    break;
                case "Inventory":
                    this.inventory.readNbtList(nbt.getList("Inventory", 10));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
