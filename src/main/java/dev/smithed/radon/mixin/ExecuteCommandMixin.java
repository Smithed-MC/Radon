package dev.smithed.radon.mixin;

import com.mojang.brigadier.ResultConsumer;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IDataCommandObjectMixin;
import net.minecraft.command.DataCommandObject;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.ExecuteCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.BinaryOperator;
import java.util.function.IntFunction;

@Mixin(ExecuteCommand.class)
public class ExecuteCommandMixin {

    @Shadow @Final static BinaryOperator<ResultConsumer<ServerCommandSource>> BINARY_RESULT_CONSUMER;

    /**
     * @author ImCoolYeah105
     * reroute getNbt() -> getNbtFiltered(path), then cancel main function is successful.
     */
    @Inject(
            method = "countPathMatches(Lnet/minecraft/command/DataCommandObject;Lnet/minecraft/command/argument/NbtPathArgumentType$NbtPath;)I",
            at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private static void radon_getNbt(DataCommandObject object, NbtPathArgumentType.NbtPath path, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        if(Radon.CONFIG.nbtOptimizations && object instanceof IDataCommandObjectMixin mixin) {
            NbtCompound nbt = mixin.getNbtFiltered(path.toString());
            Radon.logDebug(nbt);
            if(nbt != null)
                cir.setReturnValue(path.count(nbt));
        }
    }


    /**
     * @author ImCoolYeah105
     * reroute getNbt() -> getNbtFiltered(path), then cancel main function is successful.
     */
    @Inject(
            method = "executeStoreData(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/command/DataCommandObject;Lnet/minecraft/command/argument/NbtPathArgumentType$NbtPath;Ljava/util/function/IntFunction;Z)Lnet/minecraft/server/command/ServerCommandSource;",
            at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private static void radon_executeStoreData(ServerCommandSource source, DataCommandObject object, NbtPathArgumentType.NbtPath path, IntFunction<NbtElement> nbtSetter, boolean requestResult, CallbackInfoReturnable<ServerCommandSource> cir) {
        ServerCommandSource source2 = source.mergeConsumers((context, success, result) -> {
            int i = requestResult ? result : (success ? 1 : 0);
            if(Radon.CONFIG.nbtOptimizations && object instanceof IDataCommandObjectMixin mixin) {
                try {
                    NbtCompound nbtCompound = mixin.getNbtFiltered(path.toString());
                    path.put(nbtCompound, () -> nbtSetter.apply(i));
                    Radon.logDebug(nbtCompound);
                    if(mixin.setNbtFiltered(nbtCompound, path.toString()))
                        return;
                } catch (CommandSyntaxException ignored) {}
            }
            try {
                NbtCompound nbtCompound = object.getNbt();
                path.put(nbtCompound, () -> nbtSetter.apply(i));
                object.setNbt(nbtCompound);
            } catch (CommandSyntaxException ignored) {}
        }, BINARY_RESULT_CONSUMER);
        cir.setReturnValue(source2);
    }
}
