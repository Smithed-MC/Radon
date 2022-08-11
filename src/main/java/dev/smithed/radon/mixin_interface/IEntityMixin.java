package dev.smithed.radon.mixin_interface;

import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;

public interface IEntityMixin {

    NbtCompound writeFilteredNbt(NbtCompound nbt, String path);

}
