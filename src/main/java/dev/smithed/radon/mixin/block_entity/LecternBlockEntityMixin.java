package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LecternBlockEntity.class)
public abstract class LecternBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {
    @Shadow
    int currentPage;
    @Shadow
    abstract ItemStack getBook();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Book":
                    if (!this.getBook().isEmpty())
                        nbt.put("Book", this.getBook().writeNbt(new NbtCompound()));
                    break;
                case "Page":
                    if (!this.getBook().isEmpty())
                        nbt.putInt("Page", this.currentPage);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
