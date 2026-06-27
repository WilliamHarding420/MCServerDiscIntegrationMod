package dev.will.twg.commands.utils;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.will.twg.Config;
import net.minecraft.commands.CommandSourceStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ConfigValueSuggestionProvider extends StringSuggestionProvider {

    public ConfigValueSuggestionProvider() {
        super(new ArrayList<>());
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {

        Config.ConfigInfo config = Config.configNameToVariable(context.getArgument("config_name", String.class));

        if (config == null)
            return super.getSuggestions(context, builder);

        suggestions = getSuggestionsByConfig(config);

        return super.getSuggestions(context, builder);
    }

    private static List<String> getSuggestionsByConfig(Config.ConfigInfo config) {

        Object configVal = config.configValue.get();

        switch (configVal) {
            case Boolean b -> {
                return List.of("true", "false");
            }
            case List<?> configList -> {
                List<String> suggestions = new ArrayList<>();
                for (Object element : configList) {
                    suggestions.add(element.toString());
                }
                return suggestions;
            }
            default -> {
                return List.of(configVal.toString());
            }
        }

    }

}
