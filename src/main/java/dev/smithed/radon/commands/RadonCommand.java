package dev.smithed.radon.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.smithed.radon.Radon;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import static net.minecraft.server.command.CommandManager.literal;

public class RadonCommand {


    public static LiteralCommandNode register(CommandDispatcher<ServerCommandSource> dispatcher) { // You can also return a LiteralCommandNode for use with possible redirects
        return dispatcher.register(
                literal("radon")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("nbt-optimizations").executes(RadonCommand::toggle_radon_nbt)
                        .then(CommandManager.argument("enabled", BoolArgumentType.bool()).executes(RadonCommand::set_radon_nbt)))
                .then(CommandManager.literal("debug-mode").executes(RadonCommand::toggle_debug_mode)
                        .then(CommandManager.argument("enabled", BoolArgumentType.bool()).executes(RadonCommand::set_debug_mode)))
            );
    }

    public static int toggle_radon_nbt(CommandContext<ServerCommandSource> context) {
        Text text;
        if(Radon.CONFIG.nbtOptimizations) {
            text = Text.literal("Radon NBT optimizations are now disabled");
            Radon.CONFIG.nbtOptimizations = false;
        } else {
            text = Text.literal("Enabled Radon NBT Optimizations");
            Radon.CONFIG.nbtOptimizations = true;
        }
        context.getSource().getServer().getPlayerManager().broadcast(text, false);
        return Command.SINGLE_SUCCESS;
    }

    public static int set_radon_nbt(CommandContext<ServerCommandSource> ctx) {
        Text text = Text.literal("Radon NBT optimizations have been set to: " + BoolArgumentType.getBool(ctx, "enabled"));
        Radon.CONFIG.nbtOptimizations = BoolArgumentType.getBool(ctx, "enabled");
        ctx.getSource().getServer().getPlayerManager().broadcast(text, false);
        return Command.SINGLE_SUCCESS;
    }
    public static int toggle_debug_mode(CommandContext<ServerCommandSource> context) {
        Text text;
        if(Radon.CONFIG.debug) {
            text = Text.literal("Radon Debug Mode is now disabled");
            Radon.CONFIG.debug = false;
        } else {
            text = Text.literal("Enabled Radon Debug Mode");
            Radon.CONFIG.debug = true;
        }
        context.getSource().getServer().getPlayerManager().broadcast(text, false);
        return Command.SINGLE_SUCCESS;
    }

    public static int set_debug_mode(CommandContext<ServerCommandSource> ctx) {
        Text text = Text.literal("Radon Debug Mode has been set to: " + BoolArgumentType.getBool(ctx, "enabled"));
        Radon.CONFIG.debug = BoolArgumentType.getBool(ctx, "enabled");
        ctx.getSource().getServer().getPlayerManager().broadcast(text, false);
        return Command.SINGLE_SUCCESS;
    }
}
