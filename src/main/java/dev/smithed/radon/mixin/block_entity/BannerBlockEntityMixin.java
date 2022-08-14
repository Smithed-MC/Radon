package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {
    @Shadow
    private NbtList patternListNbt;
    @Shadow
    private Text customName;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Patterns":
                    if (this.patternListNbt != null)
                        nbt.put("Patterns", this.patternListNbt);
                    break;
                case "CustomName":
                    if (this.customName != null)
                        nbt.putString("CustomName", Text.Serializer.toJson(this.customName));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
