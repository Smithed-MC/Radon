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

    @Shadow StatusEffect primary;
    @Shadow StatusEffect secondary;
    @Shadow Text customName;
    @Shadow int level;
    @Shadow ContainerLock lock;
    @Shadow static StatusEffect getPotionEffectById(int id) { return null; }

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

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Primary":
                    this.primary = getPotionEffectById(nbt.getInt("Primary"));
                    break;
                case "Secondary":
                    this.secondary = getPotionEffectById(nbt.getInt("Secondary"));
                    break;
                case "CustomName":
                    if (nbt.contains("CustomName", 8))
                        this.customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
                    break;
                case "Lock":
                    this.lock = ContainerLock.fromNbt(nbt);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
