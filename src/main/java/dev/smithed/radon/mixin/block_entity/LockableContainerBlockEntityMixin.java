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
    @Shadow
    private ContainerLock lock;
    @Shadow
    private Text customName;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        switch (topLevelNbt) {
            case "CustomName":
                if (this.customName != null)
                    nbt.putString("CustomName", Text.Serializer.toJson(this.customName));
                break;
            case "Lock":
                this.lock.writeNbt(nbt);
                break;
            default:
                return false;
        }
        return true;
    }
}
