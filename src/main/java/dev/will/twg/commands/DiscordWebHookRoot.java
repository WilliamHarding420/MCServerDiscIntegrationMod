package dev.will.twg.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.will.twg.Config;
import dev.will.twg.DiscordWebHook;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class DiscordWebHookRoot {

    public static LiteralCommandNode<CommandSourceStack> DiscordWebHookRootCommand = null;

    public void register() {

        DiscordWebHookRootCommand = DiscordWebHook.Server.getCommands().getDispatcher().register(
                Commands.literal("discordwebhook").requires(sourceStack -> sourceStack.hasPermission(3))
                        .then(
                                Commands.literal("config")
                                        .then(
                                                Commands.argument("config_name", StringArgumentType.word())
                                                        .then(
                                                                Commands.literal("get")
                                                                        .executes(this::getConfig)
                                                        )
                                                        .then(
                                                                Commands.literal("set")
                                                                        .then(
                                                                                Commands.argument("value", StringArgumentType.word())
                                                                                        .executes(this::modifyConfig)
                                                                        )
                                                        )
                                        )
                        )
        );

    }

    private int getConfig(CommandContext<CommandSourceStack> context) {

        String config_name = context.getArgument("config_name", String.class);

        ModConfigSpec.ConfigValue<?> config = configNameToVariable(config_name);

        if (config == null) {
            context.getSource().sendSystemMessage(Component.literal("Must give a valid config name."));
            return 0;
        }

        MutableComponent message = Component.literal(String.format("Value of \"%s\" is \"%s\"", config_name, config.get()));
        context.getSource().sendSystemMessage(message);

        return 0;
    }

    private int modifyConfig(CommandContext<CommandSourceStack> context) {

        String config_option = context.getArgument("config_name", String.class);
        ModConfigSpec.ConfigValue<Object> config = (ModConfigSpec.ConfigValue<Object>) configNameToVariable(config_option);

        if (config == null) {
            context.getSource().sendSystemMessage(Component.literal("Must give a valid config name."));
            return 0;
        }

        Object value = parseArgumentFromString(context.getArgument("value", String.class), config.getSpec().getClazz());

        if (value == null) {
            context.getSource().sendSystemMessage(Component.literal("Invalid config value."));
            return 0;
        }

        config.set(value);
        config.save();

        context.getSource().sendSystemMessage(Component.literal(String.format("Config option \"%s\" set to \"%s\".", config_option, value)));

        return 0;

    }

    // only contains config options currently configurable in game
    public static <T> ModConfigSpec.ConfigValue<?> configNameToVariable(String name) {

        return switch (name) {
            case "discord_web_hook" -> Config.DISCORD_WEB_HOOK;
            case "send_server_status" -> Config.SEND_SERVER_STATUS;
            case "send_player_connections" -> Config.SEND_PLAYER_CONNECTIONS;
            case "send_minecraft_chat" -> Config.SEND_MINECRAFT_MESSAGES;
            case "send_discord_chat" -> Config.SEND_DISCORD_MESSAGES;
            default -> null;
        };

    }

    // parses value to required type
    public static Object parseArgumentFromString(String value, Object to) {

        if (to == String.class)
            return value;

        if (to == Boolean.class)
            return Boolean.parseBoolean(value);

        return null;

    }

}
