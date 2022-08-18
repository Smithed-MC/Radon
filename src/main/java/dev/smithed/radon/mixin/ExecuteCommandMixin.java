package dev.smithed.radon.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IDataCommandObjectMixin;
import net.minecraft.command.DataCommandObject;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.ExecuteCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ExecuteCommand.class)
public class ExecuteCommandMixin {

    @Inject(
            method = "countPathMatches(Lnet/minecraft/command/DataCommandObject;Lnet/minecraft/command/argument/NbtPathArgumentType$NbtPath;)I",
            at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private static void getNbt(DataCommandObject object, NbtPathArgumentType.NbtPath path, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        if(Radon.CONFIG.nbtOptimizations && object instanceof IDataCommandObjectMixin mixin) {
            NbtCompound nbt = mixin.getNbtFiltered(path);
            Radon.logDebug(nbt);
            if(nbt != null)
                cir.setReturnValue(path.count(nbt));
        }
    }
}
