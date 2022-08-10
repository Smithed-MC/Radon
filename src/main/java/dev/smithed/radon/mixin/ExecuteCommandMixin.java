package dev.smithed.radon.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.smithed.radon.mixin_interface.IDataCommandObjectMixin;
import net.minecraft.command.DataCommandObject;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.server.command.ExecuteCommand;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ExecuteCommand.class)
public class ExecuteCommandMixin {

    private static int countPathMatches(DataCommandObject object, NbtPathArgumentType.NbtPath path) throws CommandSyntaxException {
        if(object instanceof IDataCommandObjectMixin idcom) {
            return path.count(idcom.getFilteredNbt(path));
        } else {
            return path.count(object.getNbt());
        }
    }
}
