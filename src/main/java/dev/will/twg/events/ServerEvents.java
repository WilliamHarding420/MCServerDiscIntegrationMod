package dev.will.twg.events;

import dev.will.twg.Config;
import dev.will.twg.DiscordWebHook;
import dev.will.twg.WebHookHelper;
import dev.will.twg.commands.DiscordWebHookRoot;
import dev.will.twg.discord.DiscordBot;
import dev.will.twg.discord.DiscordMessageReceived;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

public class ServerEvents {

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

        DiscordWebHook.LOGGER.debug("Read discord webhook and channel IDs from config.");

        DiscordWebHook.Server = event.getServer();
        Config.cacheConfigPaths();

        DiscordBot.CreateBot();

        registerCommands();

        if (Config.SEND_SERVER_STATUS.get())
            WebHookHelper.SendWebHook(Config.DISCORD_WEB_HOOK.get(), "Server has started.");


    }

    private void registerCommands() {

        new DiscordWebHookRoot().register();

    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        DiscordBot.DiscordBot.shutdownNow();

        if (Config.SEND_SERVER_STATUS.get())
            WebHookHelper.SendWebHook(Config.DISCORD_WEB_HOOK.get(), "Server shutting down.");
    }

}
