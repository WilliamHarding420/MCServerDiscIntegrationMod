package dev.will.twg.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.will.twg.Config;
import dev.will.twg.DiscordWebHook;
import dev.will.twg.commands.utils.CommandUtils;
import dev.will.twg.commands.utils.ConfigValueSuggestionProvider;
import dev.will.twg.commands.utils.StringSuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

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
                                                                .suggests(new ConfigValueSuggestionProvider())
                                                                .executes(this::modifyConfig)
                                                )
                                )
                );

        DiscordWebHookRootCommand = DiscordWebHook.Server.getCommands().getDispatcher().register(builder);

    }

    private int getConfig(CommandContext<CommandSourceStack> context) {

        String config_name = context.getArgument("config_name", String.class);

        Config.ConfigInfo configInfo = Config.configNameToVariable(config_name);

        if (configInfo == null) {
            CommandUtils.sendCommandFeedback(context, "Must give a valid config name.");
            return 0;
        }

        ModConfigSpec.ConfigValue<?> config = configInfo.configValue;

        CommandUtils.sendCommandFeedback(context, String.format("Value of \"%s\" is \"%s\"", config_name, config.get()));

        return 0;
    }

    private int modifyConfig(CommandContext<CommandSourceStack> context) {

        String config_option = context.getArgument("config_name", String.class);
        Config.ConfigInfo configInfo = Config.configNameToVariable(config_option);

        if (configInfo == null) {
            CommandUtils.sendCommandFeedback(context, "Must give a valid config name.");
            return 0;
        }

        ModConfigSpec.ConfigValue<Object> config = (ModConfigSpec.ConfigValue<Object>) configInfo.configValue;

        Class configType;
        // if the config option is a list, try get the value from ListType attribute for parsing
        if (config.get() instanceof List<?>) {
            if (configInfo.listType == null) {
                CommandUtils.sendCommandFeedback(context, "This list is not editable.");
                return 0;
            }
            configType = configInfo.listType;
        } else // if not list type, just get the config value's type
            configType = config.getSpec().getClazz();

        Object value = CommandUtils.parseArgumentFromString(context.getArgument("value", String.class), configType);

        if (value == null) {
            CommandUtils.sendCommandFeedback(context, "Invalid config value.");
            return 0;
        }

        // checking if the config value is a list type
        if (config.get() instanceof List<?> configList) {

            // removing the entered value if it exists
            if (configList.contains(value)) {

                configList.remove(value);
                CommandUtils.sendCommandFeedback(context, String.format("Removed element \"%s\" from \"%s\".", value, config.getPath()));

            }
            else { // adding the entered value if it doesn't exist, must cast to List<Object> as cant add elements to List<?>

                List<Object> objList = (List<Object>) configList;
                objList.add(value);
                CommandUtils.sendCommandFeedback(context, String.format("Added element \"%s\" to \"%s\".", value, config.getPath()));

            }

        } else { // if not a list config option, just set the value
            config.set(value);
            CommandUtils.sendCommandFeedback(context, String.format("Config option \"%s\" set to \"%s\".", config_option, value));
        }

        config.save();

        return 0;

    }

}
