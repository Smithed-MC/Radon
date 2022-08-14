package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnchantingTableBlockEntity.class)
public abstract class EnchantingTableBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {
    @Shadow
    private Text customName;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        EnchantingTableBlockEntity entity = ((EnchantingTableBlockEntity) (Object) this);

        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "CustomName":
                    if (entity.hasCustomName())
                        nbt.putString("CustomName", Text.Serializer.toJson(this.customName));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
