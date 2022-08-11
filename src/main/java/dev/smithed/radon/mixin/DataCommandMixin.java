package dev.smithed.radon.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.smithed.radon.commands.RadonCommand;
import dev.smithed.radon.mixin_interface.IDataCommandObjectMixin;
import net.minecraft.command.DataCommandObject;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.DataCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Iterator;

@Mixin(DataCommand.class)
public class DataCommandMixin {

    @Shadow
    private static SimpleCommandExceptionType GET_MULTIPLE_EXCEPTION;

    private static NbtElement getNbt(NbtPathArgumentType.NbtPath path, DataCommandObject object) throws CommandSyntaxException {
        Collection<NbtElement> collection;
        if(RadonCommand.getIsEnabled() && object instanceof IDataCommandObjectMixin mixin) {
            collection = path.get(mixin.getFilteredNbt(path));
        } else {
            collection = path.get(object.getNbt());
        }

        Iterator<NbtElement> iterator = collection.iterator();
        NbtElement nbtElement = (NbtElement)iterator.next();
        if (iterator.hasNext()) {
            throw GET_MULTIPLE_EXCEPTION.create();
        } else {
            return nbtElement;
        }
    }

}
