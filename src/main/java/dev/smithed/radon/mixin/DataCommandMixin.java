package dev.smithed.radon.mixin;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.smithed.radon.Radon;
import dev.smithed.radon.commands.RadonCommand;
import dev.smithed.radon.mixin_interface.IDataCommandObjectMixin;
import net.minecraft.command.DataCommandObject;
import net.minecraft.command.argument.NbtElementArgumentType;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.DataCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

@Mixin(DataCommand.class)
public class DataCommandMixin {

    @Shadow
    private static SimpleCommandExceptionType GET_MULTIPLE_EXCEPTION;
    @Shadow
    private static List<DataCommand.ObjectType> TARGET_OBJECT_TYPES;
    @Shadow
    private static List<DataCommand.ObjectType> SOURCE_OBJECT_TYPES;
    @Shadow
    private static int executeModify(CommandContext<ServerCommandSource> context, DataCommand.ObjectType objectType, DataCommand.ModifyOperation modifier, List<NbtElement> elements) { return 0; }

    /**
     * @author ImCoolYeah105
     * @reason rapid testing
     * TODO: remove method override
     */
    @Overwrite
    private static NbtElement getNbt(NbtPathArgumentType.NbtPath path, DataCommandObject object) throws CommandSyntaxException {
        Collection<NbtElement> collection;
        if(Radon.CONFIG.getNbtOptimizationsEnabled() && object instanceof IDataCommandObjectMixin mixin) {
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

    /**
     * @author ImCoolYeah105
     * @reason rapid testing
     * TODO: remove method override
     */
    @Overwrite
    private static ArgumentBuilder<ServerCommandSource, ?> addModifyArgument(BiConsumer<ArgumentBuilder<ServerCommandSource, ?>, DataCommand.ModifyArgumentCreator> subArgumentAdder) {
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("modify");
        Iterator var2 = TARGET_OBJECT_TYPES.iterator();

        while(var2.hasNext()) {
            DataCommand.ObjectType objectType = (DataCommand.ObjectType)var2.next();
            objectType.addArgumentsToBuilder(literalArgumentBuilder, (builder) -> {
                ArgumentBuilder<ServerCommandSource, ?> argumentBuilder = CommandManager.argument("targetPath", NbtPathArgumentType.nbtPath());
                Iterator var4 = SOURCE_OBJECT_TYPES.iterator();

                while(var4.hasNext()) {
                    DataCommand.ObjectType objectType2 = (DataCommand.ObjectType)var4.next();
                    subArgumentAdder.accept(argumentBuilder, (modifier) -> {
                        return objectType2.addArgumentsToBuilder(CommandManager.literal("from"), (builder2) -> {
                            return builder2.executes((context) -> {
                                List<NbtElement> list = Collections.singletonList(objectType2.getObject(context).getNbt());
                                return executeModify(context, objectType, modifier, list);
                            }).then(CommandManager.argument("sourcePath", NbtPathArgumentType.nbtPath()).executes((context) -> {
                                DataCommandObject dataCommandObject = objectType2.getObject(context);
                                NbtPathArgumentType.NbtPath nbtPath = NbtPathArgumentType.getNbtPath(context, "sourcePath");

                                NbtElement nbt;
                                if(Radon.CONFIG.getNbtOptimizationsEnabled() && dataCommandObject instanceof IDataCommandObjectMixin mixin) {
                                    nbt = mixin.getFilteredNbt(nbtPath);
                                } else {
                                    nbt = dataCommandObject.getNbt();
                                }
                                List<NbtElement> list = nbtPath.get(nbt);
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

}
