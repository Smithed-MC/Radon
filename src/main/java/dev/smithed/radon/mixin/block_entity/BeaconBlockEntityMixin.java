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
                case "Primary" -> nbt.putInt("Primary", StatusEffect.getRawIdNullable(this.primary));
                case "Secondary" -> nbt.putInt("Secondary", StatusEffect.getRawIdNullable(this.secondary));
                case "Levels" -> nbt.putInt("Levels", this.level);
                case "CustomName" -> {
                    if (this.customName != null)
                        nbt.putString("CustomName", Text.Serializer.toJson(this.customName));
                }
                case "Lock" -> this.lock.writeNbt(nbt);
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
                case "Primary" -> this.primary = getPotionEffectById(nbt.getInt("Primary"));
                case "Secondary" -> this.secondary = getPotionEffectById(nbt.getInt("Secondary"));
                case "CustomName" -> {
                    if (nbt.contains("CustomName", 8))
                        this.customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
                }
                case "Lock" -> this.lock = ContainerLock.fromNbt(nbt);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
