package dev.will.twg.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StringSuggestionProvider implements SuggestionProvider<CommandSourceStack> {

    List<String> suggestions;
    public StringSuggestionProvider(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {

        for ( String suggestion : suggestions ) {
            builder.suggest(suggestion);
        }

        return builder.buildFuture();
    }
}
