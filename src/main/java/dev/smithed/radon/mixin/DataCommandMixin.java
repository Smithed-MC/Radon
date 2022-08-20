package dev.smithed.radon.mixin;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IDataCommandObjectMixin;
import dev.smithed.radon.mixin_interface.IEntityMixin;
import dev.smithed.radon.utils.NBTUtils;
import net.minecraft.command.DataCommandObject;
import net.minecraft.command.argument.NbtElementArgumentType;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.DataCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

@Mixin(DataCommand.class)
public abstract class DataCommandMixin {

    @Shadow @Final static SimpleCommandExceptionType MERGE_FAILED_EXCEPTION;
    @Shadow @Final static SimpleCommandExceptionType GET_MULTIPLE_EXCEPTION;
    @Shadow @Final static List<DataCommand.ObjectType> TARGET_OBJECT_TYPES;
    @Shadow @Final static List<DataCommand.ObjectType> SOURCE_OBJECT_TYPES;
    @Shadow static int executeModify(CommandContext<ServerCommandSource> context, DataCommand.ObjectType objectType, DataCommand.ModifyOperation modifier, List<NbtElement> elements) { return 0; }

    /**
     * @author ImCoolYeah105
     * @reason there does not appear to be a clean way to replace the getNbt() call with a getFilteredNbt(path) call
     */
    @Overwrite
    private static NbtElement getNbt(NbtPathArgumentType.NbtPath path, DataCommandObject object) throws CommandSyntaxException {
        //inject getNbt() -> getFilteredNbt(path)
        NbtCompound nbt = null;
        if (Radon.CONFIG.nbtOptimizations && object instanceof IDataCommandObjectMixin mixin)
            nbt = mixin.getNbtFiltered(path.toString());
        if (nbt == null)
            nbt = object.getNbt();
        Radon.logDebug(nbt);
        Collection<NbtElement> collection = path.get(nbt);
        //END

        Iterator<NbtElement> iterator = collection.iterator();
        NbtElement nbtElement = (NbtElement) iterator.next();
        if (iterator.hasNext()) {
            throw GET_MULTIPLE_EXCEPTION.create();
        } else {
            return nbtElement;
        }
    }

    /**
     * @author ImCoolYeah105
     * @reason there does not appear to be a clean way to replace the getNbt() call with a getFilteredNbt(path) call
     */
    @Overwrite
    private static ArgumentBuilder<ServerCommandSource, ?> addModifyArgument(BiConsumer<ArgumentBuilder<ServerCommandSource, ?>, DataCommand.ModifyArgumentCreator> subArgumentAdder) {
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("modify");
        Iterator var2 = TARGET_OBJECT_TYPES.iterator();

        while (var2.hasNext()) {
            DataCommand.ObjectType objectType = (DataCommand.ObjectType) var2.next();
            objectType.addArgumentsToBuilder(literalArgumentBuilder, (builder) -> {
                ArgumentBuilder<ServerCommandSource, ?> argumentBuilder = CommandManager.argument("targetPath", NbtPathArgumentType.nbtPath());
                Iterator var4 = SOURCE_OBJECT_TYPES.iterator();

                while (var4.hasNext()) {
                    DataCommand.ObjectType objectType2 = (DataCommand.ObjectType) var4.next();
                    subArgumentAdder.accept(argumentBuilder, (modifier) -> {
                        return objectType2.addArgumentsToBuilder(CommandManager.literal("from"), (builder2) -> {
                            return builder2.executes((context) -> {
                                List<NbtElement> list = Collections.singletonList(objectType2.getObject(context).getNbt());
                                return executeModify(context, objectType, modifier, list);
                            }).then(CommandManager.argument("sourcePath", NbtPathArgumentType.nbtPath()).executes((context) -> {
                                DataCommandObject dataCommandObject = objectType2.getObject(context);
                                NbtPathArgumentType.NbtPath nbtPath = NbtPathArgumentType.getNbtPath(context, "sourcePath");

                                //inject getNbt() -> getFilteredNbt(path)
                                NbtElement nbt = null;
                                if (Radon.CONFIG.nbtOptimizations && dataCommandObject instanceof IDataCommandObjectMixin mixin) {
                                    nbt = mixin.getNbtFiltered(nbtPath.toString());
                                }
                                if (nbt == null)
                                    nbt = dataCommandObject.getNbt();
                                Radon.logDebug(nbt);
                                List<NbtElement> list = nbtPath.get(nbt);
                                // END
                                return executeModify(context, objectType, modifier, list);
                            }));
                        });
                    });
                }

                subArgumentAdder.accept(argumentBuilder, (modifier) -> {
                    return CommandManager.literal("value").then(CommandManager.argument("value", NbtElementArgumentType.nbtElement()).executes((context) -> {
                        List<NbtElement> list = Collections.singletonList(NbtElementArgumentType.getNbtElement(context, "value"));
                        return executeModify(context, objectType, modifier, list);
                    }));
                });
                return builder.then(argumentBuilder);
            });
        }
        return literalArgumentBuilder;
    }

    /**
     * @author ImCoolYeah105
     * Overrides default method. Reads filtered data and cancels main function if successful.
     * Otherwise, main function runs normally.
     */
    @Inject(
            //method = "executeModify(Lcom/mojang/brigadier/context/CommandContext;Lcom/mojang/brigadier/context/CommandContext$ObjectType;Lcom/mojang/brigadier/context/CommandContext$ModifyOperation;Ljava/util/List;)I",
            method = "executeModify",
            at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private static void radon_executeModify(CommandContext<ServerCommandSource> context, DataCommand.ObjectType objectType, DataCommand.ModifyOperation modifier, List<NbtElement> elements, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        DataCommandObject dataCommandObject = objectType.getObject(context);
        if (Radon.CONFIG.nbtOptimizations && dataCommandObject instanceof IDataCommandObjectMixin mixin) {
            NbtPathArgumentType.NbtPath nbtPath = NbtPathArgumentType.getNbtPath(context, "targetPath");
            NbtCompound nbtCompound = mixin.getNbtFiltered(nbtPath.toString());
            if (nbtCompound != null) {
                int i = modifier.modify(context, nbtCompound, nbtPath, elements);
                if (i != 0) {
                    Radon.logDebug(nbtCompound);
                    if (mixin.setNbtFiltered(nbtCompound, nbtPath.toString())) {
                        context.getSource().sendFeedback(dataCommandObject.feedbackModify(), true);
                        cir.setReturnValue(i);
                    }
                } else {
                    throw MERGE_FAILED_EXCEPTION.create();
                }
            }
        }
    }

    /**
     * @author ImCoolYeah105
     * Overrides default method. Reads filtered data and cancels main function if successful.
     * Otherwise, main function runs normally.
     */
    @Inject(
            method = "executeMerge(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/command/DataCommandObject;Lnet/minecraft/nbt/NbtCompound;)I",
            at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private static void radon_executeMerge(ServerCommandSource source, DataCommandObject object, NbtCompound nbt, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        if (Radon.CONFIG.nbtOptimizations && object instanceof IDataCommandObjectMixin mixin) {
            String[] topLevelNbt = NBTUtils.getTopLevelPaths(nbt);
            int same = 0;
            for(String topNbt: topLevelNbt) {
                NbtCompound nbtCompound = mixin.getNbtFiltered(topNbt);
                NbtCompound nbtCompound2 = nbtCompound.copy().copyFrom(nbt.getCompound(topNbt));
                Radon.logDebug(nbtCompound2);
                if(nbtCompound.equals(nbtCompound2)) {
                    same += 1;
                } else {
                    if (!mixin.setNbtFiltered(nbtCompound, topNbt))
                        return;
                }
            }
            if(same == topLevelNbt.length)
                throw MERGE_FAILED_EXCEPTION.create();
            source.sendFeedback(object.feedbackModify(), true);
            cir.setReturnValue(1);
        }
    }

    /**
     * @author ImCoolYeah105
     * Overrides default method. Reads filtered data and cancels main function if successful.
     * Otherwise, main function runs normally.
     */
    @Inject(
            method = "executeRemove(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/command/DataCommandObject;Lnet/minecraft/command/argument/NbtPathArgumentType$NbtPath;)I",
            at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private static void radon_executeRemove(ServerCommandSource source, DataCommandObject object, NbtPathArgumentType.NbtPath path, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        if (Radon.CONFIG.nbtOptimizations && object instanceof IDataCommandObjectMixin mixin) {
            NbtCompound nbtCompound = mixin.getNbtFiltered(path.toString());
            int i = path.remove(nbtCompound);
            Radon.logDebug(nbtCompound);
            if (i != 0) {
                if(mixin.setNbtFiltered(nbtCompound, path.toString())) {
                    source.sendFeedback(object.feedbackModify(), true);
                    cir.setReturnValue(1);
                }
            }
        }
    }
}