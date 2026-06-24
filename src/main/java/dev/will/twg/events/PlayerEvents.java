package dev.will.twg.events;

import dev.will.twg.Config;
import dev.will.twg.DiscordWebHook;
import dev.will.twg.WebHookHelper;
import dev.will.twg.discord.DiscordBot;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class PlayerEvents {

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {

        DiscordWebHook.LOGGER.debug("Executing player join event.");

        Player player = event.getEntity();

        String webHookMessage = String.format("%s joined.", player.getDisplayName().getString());

        DiscordBot.UpdateBotStatusPlayerCount(DiscordWebHook.Server.getPlayerCount());

        if (Config.SEND_PLAYER_CONNECTIONS.get())
            WebHookHelper.SendWebHook(WebHookHelper.DiscordWebHookURL, webHookMessage);

    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {

        DiscordWebHook.LOGGER.debug("Executing player left event.");

        Player player = event.getEntity();

        String webHookMessage = String.format("%s left.", player.getDisplayName().getString());

        DiscordBot.UpdateBotStatusPlayerCount(DiscordWebHook.Server.getPlayerCount() - 1);

        if (Config.SEND_PLAYER_CONNECTIONS.get())
            WebHookHelper.SendWebHook(WebHookHelper.DiscordWebHookURL, webHookMessage);

    }

    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event) {

        DiscordWebHook.LOGGER.debug("Executing player chat event.");

        ServerPlayer player = event.getPlayer();

        String webHookMessage = String.format("%s: %s", player.getDisplayName().getString(), event.getMessage().getString());

        if(Config.SEND_MINECRAFT_MESSAGES.get())
            WebHookHelper.SendWebHook(WebHookHelper.DiscordWebHookURL, webHookMessage);

    }

}
