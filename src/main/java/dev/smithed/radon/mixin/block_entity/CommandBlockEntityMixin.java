package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.CommandBlockExecutor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CommandBlockBlockEntity.class)
public abstract class CommandBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {

    @Shadow @Final CommandBlockExecutor commandExecutor;
    @Shadow boolean powered;
    @Shadow boolean conditionMet;
    @Shadow abstract boolean isPowered();
    @Shadow abstract boolean isAuto();
    @Shadow abstract boolean isConditionMet();
    @Shadow abstract void setAuto(boolean auto);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "powered":
                    nbt.putBoolean("powered", this.isPowered());
                    break;
                case "conditionMet":
                    nbt.putBoolean("conditionMet", this.isConditionMet());
                    break;
                case "auto":
                    nbt.putBoolean("auto", this.isAuto());
                    break;
                case "Command":
                case "SuccessCount":
                case "CustomName":
                case "TrackOutput":
                case "LastOutput":
                case "UpdateLastExecution":
                case "LastExecution":
                    this.commandExecutor.writeNbt(nbt);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Command":
                case "SuccessCount":
                case "CustomName":
                case "TrackOutput":
                case "LastOutput":
                case "UpdateLastExecution":
                case "LastExecution":
                    this.commandExecutor.readNbt(nbt);
                    break;
                case "powered":
                    this.powered = nbt.getBoolean("powered");
                    break;
                case "conditionMet":
                    this.conditionMet = nbt.getBoolean("conditionMet");
                    break;
                case "auto":
                    this.setAuto(nbt.getBoolean("auto"));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
