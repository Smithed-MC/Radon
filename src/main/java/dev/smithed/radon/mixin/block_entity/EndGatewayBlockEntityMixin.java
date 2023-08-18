package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EndGatewayBlockEntity.class)
public abstract class EndGatewayBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {

    @Shadow long age;
    @Shadow BlockPos exitPortalPos;
    @Shadow boolean exactTeleport;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Age" -> nbt.putLong("Age", this.age);
                case "ExitPortal" -> {
                    if (this.exitPortalPos != null)
                        nbt.put("ExitPortal", NbtHelper.fromBlockPos(this.exitPortalPos));
                }
                case "ExactTeleport" -> {
                    if (this.exactTeleport)
                        nbt.putBoolean("ExactTeleport", true);
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
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Age" -> this.age = nbt.getLong("Age");
                case "ExitPortal" -> {
                    if (nbt.contains("ExitPortal", 10)) {
                        BlockPos blockPos = NbtHelper.toBlockPos(nbt.getCompound("ExitPortal"));
                        if (World.isValid(blockPos)) {
                            this.exitPortalPos = blockPos;
                        }
                    }
                }
                case "ExactTeleport" -> this.exactTeleport = nbt.getBoolean("ExactTeleport");
                default -> {
                    return false;
                }
            }

        }
        return true;
    }
}
