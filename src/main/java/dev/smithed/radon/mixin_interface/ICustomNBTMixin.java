package dev.smithed.radon.mixin_interface;

import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;

public interface ICustomNBTMixin {

    boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt);

}
