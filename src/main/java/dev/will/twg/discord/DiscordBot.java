package dev.will.twg.discord;

import dev.will.twg.Config;
import dev.will.twg.DiscordWebHook;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;

public class DiscordBot {

    public static JDA DiscordBot = null;

    public static void CreateBot() {

        String token = Config.DISCORD_BOT_TOKEN.get();
        DiscordWebHook.LOGGER.debug("Read discord bot token from config.");

        if (token.isEmpty()) {
            DiscordWebHook.LOGGER.info("You must provide a discord bot token.");
            return;
        }

        if (DiscordBot != null) {
            DiscordWebHook.LOGGER.debug("Discord bot already created.");
            return;
        }

        DiscordWebHook.LOGGER.info("Starting discord bot setup.");
        try {
            DiscordBot = JDABuilder.createLight(token, EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
                    .addEventListeners(new DiscordSlashCommand())
                    .addEventListeners(new DiscordMessageReceived())
                    .build();
        } catch (Exception e) {
            DiscordWebHook.LOGGER.info("Failed to create discord bot. {}", e.getMessage());
            return;
        }

        UpdateBotStatusPlayerCount(DiscordWebHook.Server.getPlayerCount());

        DiscordWebHook.LOGGER.info("Adding bot commands.");
        CommandListUpdateAction commands = DiscordBot.updateCommands();
        commands = commands.addCommands(
                Commands.slash("players", "Lists the currently online players.")
                        .setContexts(InteractionContextType.GUILD)
                        .setDefaultPermissions(DefaultMemberPermissions.ENABLED)
        );
        commands.queue();

        DiscordWebHook.LOGGER.info("Discord bot setup successfully.");

    }

    public static CompletableFuture<Void> UpdateBotStatusPlayerCount(int playerCount) {

        DiscordWebHook.LOGGER.debug("Updating bot status.");

        return CompletableFuture.supplyAsync(() -> {
            DiscordBot.getPresence().setActivity(Activity.customStatus(String.format("Players Online: %d", playerCount)));
            return null;
        });

    }

}
