package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.inventory.ContainerLock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {
    @Shadow
    StatusEffect primary;
    @Shadow
    StatusEffect secondary;
    @Shadow
    private Text customName;
    @Shadow
    int level;
    @Shadow
    private ContainerLock lock;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Primary":
                    nbt.putInt("Primary", StatusEffect.method_43257(this.primary));
                    break;
                case "Secondary":
                    nbt.putInt("Secondary", StatusEffect.method_43257(this.secondary));
                    break;
                case "Levels":
                    nbt.putInt("Levels", this.level);
                    break;
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
        }
        return true;
    }
}
