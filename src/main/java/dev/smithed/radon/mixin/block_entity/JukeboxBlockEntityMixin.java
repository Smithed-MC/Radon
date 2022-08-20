package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {

    @Shadow long tickCount;
    @Shadow long recordStartTick;
    @Shadow boolean isPlaying;
    @Shadow abstract ItemStack getRecord();
    @Shadow abstract void setRecord(ItemStack stack);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "RecordItem":
                    if (!this.getRecord().isEmpty())
                        nbt.put("RecordItem", this.getRecord().writeNbt(new NbtCompound()));
                    break;
                case "IsPlaying":
                    nbt.putBoolean("IsPlaying", this.isPlaying);
                    break;
                case "RecordStartTick":
                    nbt.putLong("RecordStartTick", this.recordStartTick);
                    break;
                case "TickCount":
                    nbt.putLong("TickCount", this.tickCount);
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
                case "RecordItem":
                    if (nbt.contains("RecordItem", 10))
                        this.setRecord(ItemStack.fromNbt(nbt.getCompound("RecordItem")));
                    break;
                case "IsPlaying":
                    this.isPlaying = nbt.getBoolean("IsPlaying");
                    break;
                case "RecordStartTick":
                    this.recordStartTick = nbt.getLong("RecordStartTick");
                    break;
                case "TickCount":
                    this.tickCount = nbt.getLong("TickCount");
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
