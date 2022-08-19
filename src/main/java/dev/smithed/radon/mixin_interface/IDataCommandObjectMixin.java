package dev.smithed.radon.mixin_interface;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;

public interface IDataCommandObjectMixin {

    NbtCompound getNbtFiltered(String path) throws CommandSyntaxException;
    boolean setNbtFiltered(NbtCompound nbt, String path) throws CommandSyntaxException;

}
