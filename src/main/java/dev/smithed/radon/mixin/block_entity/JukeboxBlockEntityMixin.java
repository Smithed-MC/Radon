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
    @Shadow ItemStack recordStack;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        final JukeboxBlockEntity entity = ((JukeboxBlockEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "RecordItem" -> {
                    if (!entity.getStack().isEmpty())
                        nbt.put("RecordItem", entity.getStack().writeNbt(new NbtCompound()));
                }
                case "IsPlaying" -> nbt.putBoolean("IsPlaying", this.isPlaying);
                case "RecordStartTick" -> nbt.putLong("RecordStartTick", this.recordStartTick);
                case "TickCount" -> nbt.putLong("TickCount", this.tickCount);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "RecordItem" -> {
                    if (nbt.contains("RecordItem", 10))
                        this.recordStack = ItemStack.fromNbt(nbt.getCompound("RecordItem"));
                }
                case "IsPlaying" -> this.isPlaying = nbt.getBoolean("IsPlaying");
                case "RecordStartTick" -> this.recordStartTick = nbt.getLong("RecordStartTick");
                case "TickCount" -> this.tickCount = nbt.getLong("TickCount");
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
