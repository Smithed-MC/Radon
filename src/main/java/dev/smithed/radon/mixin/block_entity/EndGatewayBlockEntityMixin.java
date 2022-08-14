package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EndGatewayBlockEntity.class)
public abstract class EndGatewayBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {
    @Shadow
    private long age;
    @Shadow
    private BlockPos exitPortalPos;
    @Shadow
    private boolean exactTeleport;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Age":
                    nbt.putLong("Age", this.age);
                    break;
                case "ExitPortal":
                    if (this.exitPortalPos != null)
                        nbt.put("ExitPortal", NbtHelper.fromBlockPos(this.exitPortalPos));
                    break;
                case "ExactTeleport":
                    if (this.exactTeleport)
                        nbt.putBoolean("ExactTeleport", true);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
