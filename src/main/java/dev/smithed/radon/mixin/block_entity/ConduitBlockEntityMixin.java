package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.ConduitBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(ConduitBlockEntity.class)
public abstract class ConduitBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {

    @Shadow LivingEntity targetEntity;
    @Shadow UUID targetUuid;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("Target")) {
                if (this.targetEntity != null)
                    nbt.putUuid("Target", this.targetEntity.getUuid());
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("Target")) {
                if (nbt.containsUuid("Target")) {
                    this.targetUuid = nbt.getUuid("Target");
                } else {
                    this.targetUuid = null;
                }
            } else {
                return false;
            }
        }
        return true;
    }
}
