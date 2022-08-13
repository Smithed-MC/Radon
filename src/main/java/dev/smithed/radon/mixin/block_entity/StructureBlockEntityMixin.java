package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StructureBlockBlockEntity.class)
public abstract class StructureBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {
    @Shadow
    private String author = "";
    @Shadow
    private String metadata = "";
    @Shadow
    private BlockPos offset = new BlockPos(0, 1, 0);
    @Shadow
    private Vec3i size;
    @Shadow
    private BlockMirror mirror;
    @Shadow
    private BlockRotation rotation;
    @Shadow
    private StructureBlockMode mode;
    @Shadow
    private boolean ignoreEntities;
    @Shadow
    private boolean powered;
    @Shadow
    private boolean showAir;
    @Shadow
    private boolean showBoundingBox;
    @Shadow
    private float integrity;
    @Shadow
    private long seed;
    @Shadow
    abstract String getTemplateName();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "name":
                    nbt.putString("name", this.getTemplateName());
                    break;
                case "author":
                    nbt.putString("author", this.author);
                    break;
                case "metadata":
                    nbt.putString("metadata", this.metadata);
                    break;
                case "posX":
                    nbt.putInt("posX", this.offset.getX());
                    break;
                case "posY":
                    nbt.putInt("posY", this.offset.getY());
                    break;
                case "posZ":
                    nbt.putInt("posZ", this.offset.getZ());
                    break;
                case "sizeX":
                    nbt.putInt("sizeX", this.size.getX());
                    break;
                case "sizeY":
                    nbt.putInt("sizeY", this.size.getY());
                    break;
                case "sizeZ":
                    nbt.putInt("sizeZ", this.size.getZ());
                    break;
                case "rotation":
                    nbt.putString("rotation", this.rotation.toString());
                    break;
                case "mirror":
                    nbt.putString("mirror", this.mirror.toString());
                    break;
                case "mode":
                    nbt.putString("mode", this.mode.toString());
                    break;
                case "ignoreEntities":
                    nbt.putBoolean("ignoreEntities", this.ignoreEntities);
                    break;
                case "powered":
                    nbt.putBoolean("powered", this.powered);
                    break;
                case "showair":
                    nbt.putBoolean("showair", this.showAir);
                    break;
                case "showboundingbox":
                    nbt.putBoolean("showboundingbox", this.showBoundingBox);
                    break;
                case "integrity":
                    nbt.putFloat("integrity", this.integrity);
                    break;
                case "seed":
                    nbt.putLong("seed", this.seed);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
