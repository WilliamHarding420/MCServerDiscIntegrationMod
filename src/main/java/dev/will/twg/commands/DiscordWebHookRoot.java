package dev.will.twg.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.will.twg.Config;
import dev.will.twg.DiscordWebHook;
import dev.will.twg.commands.utils.CommandUtils;
import dev.will.twg.commands.utils.StringSuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class DiscordWebHookRoot {

    public static LiteralCommandNode<CommandSourceStack> DiscordWebHookRootCommand = null;

    public void register() {

        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("discordwebhook").requires(sourceStack -> sourceStack.hasPermission(3))
                .then(
                        Commands.literal("config")
                                .then(
                                        Commands.argument("config_name", StringArgumentType.word())
                                                .suggests(new StringSuggestionProvider(Config.configPaths))
                                                .executes(this::getConfig)
                                                .then(
                                                        Commands.argument("value", StringArgumentType.word())
                                                                .executes(this::modifyConfig)
                                                )
                                )
                );

        DiscordWebHookRootCommand = DiscordWebHook.Server.getCommands().getDispatcher().register(builder);

    }

    private int getConfig(CommandContext<CommandSourceStack> context) {

        String config_name = context.getArgument("config_name", String.class);

        ModConfigSpec.ConfigValue<?> config = Config.configNameToVariable(config_name);

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
        ModConfigSpec.ConfigValue<Object> config = (ModConfigSpec.ConfigValue<Object>) Config.configNameToVariable(config_option);

        if (config == null) {
            context.getSource().sendSystemMessage(Component.literal("Must give a valid config name."));
            return 0;
        }

        Object value = CommandUtils.parseArgumentFromString(context.getArgument("value", String.class), config.getSpec().getClazz());

        if (value == null) {
            context.getSource().sendSystemMessage(Component.literal("Invalid config value."));
            return 0;
        }

        config.set(value);
        config.save();

        context.getSource().sendSystemMessage(Component.literal(String.format("Config option \"%s\" set to \"%s\".", config_option, value)));

        return 0;

    }

}
