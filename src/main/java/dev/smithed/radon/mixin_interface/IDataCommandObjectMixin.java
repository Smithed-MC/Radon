package dev.smithed.radon.mixin_interface;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;

public interface IDataCommandObjectMixin {

    NbtCompound getFilteredNbt(NbtPathArgumentType.NbtPath path) throws CommandSyntaxException;

}
