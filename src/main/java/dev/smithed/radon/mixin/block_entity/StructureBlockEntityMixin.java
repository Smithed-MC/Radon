package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StructureBlockBlockEntity.class)
public abstract class StructureBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {

    @Shadow String author = "";
    @Shadow String metadata = "";
    @Shadow BlockPos offset = new BlockPos(0, 1, 0);
    @Shadow Vec3i size;
    @Shadow BlockMirror mirror;
    @Shadow BlockRotation rotation;
    @Shadow StructureBlockMode mode;
    @Shadow boolean ignoreEntities;
    @Shadow boolean powered;
    @Shadow boolean showAir;
    @Shadow boolean showBoundingBox;
    @Shadow float integrity;
    @Shadow long seed;
    @Shadow abstract String getTemplateName();
    @Shadow abstract void setTemplateName(@Nullable String templateName);
    @Shadow abstract void updateBlockMode();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "name" -> nbt.putString("name", this.getTemplateName());
                case "author" -> nbt.putString("author", this.author);
                case "metadata" -> nbt.putString("metadata", this.metadata);
                case "posX" -> nbt.putInt("posX", this.offset.getX());
                case "posY" -> nbt.putInt("posY", this.offset.getY());
                case "posZ" -> nbt.putInt("posZ", this.offset.getZ());
                case "sizeX" -> nbt.putInt("sizeX", this.size.getX());
                case "sizeY" -> nbt.putInt("sizeY", this.size.getY());
                case "sizeZ" -> nbt.putInt("sizeZ", this.size.getZ());
                case "rotation" -> nbt.putString("rotation", this.rotation.toString());
                case "mirror" -> nbt.putString("mirror", this.mirror.toString());
                case "mode" -> nbt.putString("mode", this.mode.toString());
                case "ignoreEntities" -> nbt.putBoolean("ignoreEntities", this.ignoreEntities);
                case "powered" -> nbt.putBoolean("powered", this.powered);
                case "showair" -> nbt.putBoolean("showair", this.showAir);
                case "showboundingbox" -> nbt.putBoolean("showboundingbox", this.showBoundingBox);
                case "integrity" -> nbt.putFloat("integrity", this.integrity);
                case "seed" -> nbt.putLong("seed", this.seed);
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
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "name" -> this.setTemplateName(nbt.getString("name"));
                case "author" -> this.author = nbt.getString("author");
                case "metadata" -> this.metadata = nbt.getString("metadata");
                case "posX", "posY", "posZ" -> {
                    int i = nbt.contains("posX") ? MathHelper.clamp(nbt.getInt("posX"), -48, 48) : this.offset.getX();
                    int j = nbt.contains("posY") ? MathHelper.clamp(nbt.getInt("posY"), -48, 48) : this.offset.getY();
                    int k = nbt.contains("posZ") ? MathHelper.clamp(nbt.getInt("posZ"), -48, 48) : this.offset.getZ();
                    this.offset = new BlockPos(i, j, k);
                }
                case "sizeX", "sizeY", "sizeZ" -> {
                    int l = nbt.contains("posX") ? MathHelper.clamp(nbt.getInt("posX"), 0, 48) : this.size.getX();
                    int m = nbt.contains("posY") ? MathHelper.clamp(nbt.getInt("posY"), 0, 48) : this.size.getY();
                    int n = nbt.contains("posZ") ? MathHelper.clamp(nbt.getInt("posZ"), 0, 48) : this.size.getZ();
                    this.size = new Vec3i(l, m, n);
                }
                case "rotation" -> {
                    try {
                        this.rotation = BlockRotation.valueOf(nbt.getString("rotation"));
                    } catch (IllegalArgumentException var11) {
                        this.rotation = BlockRotation.NONE;
                    }
                }
                case "mirror" -> {
                    try {
                        this.mirror = BlockMirror.valueOf(nbt.getString("mirror"));
                    } catch (IllegalArgumentException var10) {
                        this.mirror = BlockMirror.NONE;
                    }
                }
                case "mode" -> {
                    try {
                        this.mode = StructureBlockMode.valueOf(nbt.getString("mode"));
                    } catch (IllegalArgumentException var9) {
                        this.mode = StructureBlockMode.DATA;
                    }
                }
                case "ignoreEntities" -> this.ignoreEntities = nbt.getBoolean("ignoreEntities");
                case "powered" -> this.powered = nbt.getBoolean("powered");
                case "showair" -> this.showAir = nbt.getBoolean("showair");
                case "showboundingbox" -> this.showBoundingBox = nbt.getBoolean("showboundingbox");
                case "integrity" -> {
                    if (nbt.contains("integrity")) {
                        this.integrity = nbt.getFloat("integrity");
                    } else {
                        this.integrity = 1.0F;
                    }
                }
                case "seed" -> this.seed = nbt.getLong("seed");
                default -> {
                    return false;
                }
            }
            this.updateBlockMode();
        }
        return true;
    }
}
