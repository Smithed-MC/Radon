package dev.smithed.radon.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.smithed.radon.commands.RadonCommand;
import dev.smithed.radon.mixin_interface.IDataCommandObjectMixin;
import net.minecraft.command.DataCommandObject;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.server.command.ExecuteCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ExecuteCommand.class)
public class ExecuteCommandMixin {

    /**
     * @author ImCoolYeah105
     * @reason rapid testing
     * TODO: remove method override
     */
    @Overwrite
    private static int countPathMatches(DataCommandObject object, NbtPathArgumentType.NbtPath path) throws CommandSyntaxException {
        if(RadonCommand.getIsEnabled() && object instanceof IDataCommandObjectMixin mixin) {
            return path.count(mixin.getFilteredNbt(path));
        } else {
            return path.count(object.getNbt());
        }
    }
}
