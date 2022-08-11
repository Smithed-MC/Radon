package dev.smithed.radon.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import static net.minecraft.server.command.CommandManager.literal;

public class RadonCommand {

    private static boolean isEnabled = true;

    public static boolean getIsEnabled() {
        return isEnabled;
    }

    public static LiteralCommandNode register(CommandDispatcher<ServerCommandSource> dispatcher) { // You can also return a LiteralCommandNode for use with possible redirects
        return dispatcher.register(
                literal("radon")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("enable-nbt").executes((context) -> radon_enable_nbt(context)))
                .then(CommandManager.literal("disable-nbt").executes((context) -> radon_disable_nbt(context)))
            );
    }

    public static int radon_enable_nbt(CommandContext<ServerCommandSource> ctx) {
        Text text;
        if(isEnabled) {
            text = Text.literal("Radon NBT optimizations are already enabled");
        } else {
            text = Text.literal("Enabled Radon NBT Optimizations");
            isEnabled = true;
        }
        ctx.getSource().getServer().getPlayerManager().broadcast(text, false);
        return Command.SINGLE_SUCCESS;
    }

    public static int radon_disable_nbt(CommandContext<ServerCommandSource> ctx) {
        Text text;
        if(isEnabled) {
            isEnabled = false;
            text = Text.literal("Disabled Radon NBT Optimizations");
        } else {
            text = Text.literal("Radon NBT Optimizations are already disabled");
        }
        ctx.getSource().getServer().getPlayerManager().broadcast(text, false);
        return Command.SINGLE_SUCCESS;
    }
}
