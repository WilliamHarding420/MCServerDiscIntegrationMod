package dev.will.twg.discord;

import dev.will.twg.DiscordWebHook;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DiscordSlashCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        DiscordWebHook.LOGGER.debug("Executing slash command event. {}", event.getName());

        // only switch in case I want to add more commands soon, else would be an if
        switch (event.getName()) {

            case "players":
                event.reply(getPlayerString()).queue();
                break;

        }
    }

    private String getPlayerString() {
        MinecraftServer server = DiscordWebHook.Server;
        int playerCount = server.getPlayerCount();
        List<ServerPlayer> players = server.getPlayerList().getPlayers();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format("Players online (%d):\n", playerCount));

        for(ServerPlayer player : players) {
            stringBuilder.append(String.format("%s\n", player.getDisplayName().getString()));
        }

        return stringBuilder.toString();
    }
}
