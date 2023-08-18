package dev.smithed.radon.mixin.block_entity;

import com.mojang.datafixers.util.Pair;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {

    @Shadow NbtList patternListNbt;
    @Shadow Text customName;
    @Shadow List<Pair<RegistryEntry<BannerPattern>, DyeColor>> patterns;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Patterns" -> {
                    if (this.patternListNbt != null)
                        nbt.put("Patterns", this.patternListNbt);
                }
                case "CustomName" -> {
                    if (this.customName != null)
                        nbt.putString("CustomName", Text.Serializer.toJson(this.customName));
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
                case "CustomName" -> {
                    if (nbt.contains("CustomName", 8))
                        this.customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
                }
                case "Patterns" -> {
                    this.patternListNbt = nbt.getList("Patterns", 10);
                    this.patterns = null;
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
