package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.ContainerLock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LockableContainerBlockEntity.class)
public abstract class LockableContainerBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {

    @Shadow ContainerLock lock;
    @Shadow Text customName;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        switch (topLevelNbt) {
            case "CustomName" -> {
                if (this.customName != null)
                    nbt.putString("CustomName", Text.Serializer.toJson(this.customName));
            }
            case "Lock" -> this.lock.writeNbt(nbt);
            default -> {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Lock" -> this.lock = ContainerLock.fromNbt(nbt);
                case "CustomName" -> {
                    if (nbt.contains("CustomName", 8))
                        this.customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
