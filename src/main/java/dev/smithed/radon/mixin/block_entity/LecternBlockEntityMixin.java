package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LecternBlockEntity.class)
public abstract class LecternBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {

    @Shadow ItemStack book;
    @Shadow int currentPage;
    @Shadow int pageCount;
    @Shadow abstract ItemStack getBook();
    @Shadow abstract ItemStack resolveBook(ItemStack book, @Nullable PlayerEntity player);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Book" -> {
                    if (!this.getBook().isEmpty())
                        nbt.put("Book", this.getBook().writeNbt(new NbtCompound()));
                }
                case "Page" -> {
                    if (!this.getBook().isEmpty())
                        nbt.putInt("Page", this.currentPage);
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
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Book" -> {
                    if (nbt.contains("Book", 10)) {
                        this.book = this.resolveBook(ItemStack.fromNbt(nbt.getCompound("Book")), (PlayerEntity) null);
                    } else {
                        this.book = ItemStack.EMPTY;
                    }
                    this.pageCount = WrittenBookItem.getPageCount(this.book);
                }
                case "Page" -> this.currentPage = MathHelper.clamp(nbt.getInt("Page"), 0, this.pageCount - 1);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
