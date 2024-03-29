package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnchantingTableBlockEntity.class)
public abstract class EnchantingTableBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {

    @Shadow Text customName;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        EnchantingTableBlockEntity entity = ((EnchantingTableBlockEntity) (Object) this);

        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("CustomName")) {
                if (entity.hasCustomName())
                    nbt.putString("CustomName", Text.Serialization.toJsonString(this.customName));
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("CustomName")) {
                if (nbt.contains("CustomName", 8))
                    this.customName = Text.Serialization.fromJson(nbt.getString("CustomName"));
            } else {
                return false;
            }
        }
        return true;
    }
}
