package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {
    @Shadow
    private long tickCount;
    @Shadow
    private long recordStartTick;
    @Shadow
    private boolean isPlaying;
    @Shadow
    abstract ItemStack getRecord();

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
}
