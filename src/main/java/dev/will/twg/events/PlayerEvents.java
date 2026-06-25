package dev.will.twg.events;

import dev.will.twg.Config;
import dev.will.twg.DiscordWebHook;
import dev.will.twg.WebHookHelper;
import dev.will.twg.discord.DiscordBot;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
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

    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {

        DiscordWebHook.LOGGER.debug("Executing player advancement event.");

        Player player = event.getEntity();

        String webHookMessage = String.format("%s completed advancement %s.",
                player.getDisplayName().getString(),
                event.getAdvancement().value().name().get().getString());

        if (Config.SEND_ADVANCEMENT_MESSAGES.get())
            WebHookHelper.SendWebHook(WebHookHelper.DiscordWebHookURL, webHookMessage);

    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {

        if (!(event.getEntity() instanceof Player))
            return;

        DiscordWebHook.LOGGER.debug("Executing living death event.");

        Component deathMessage = event.getSource().getLocalizedDeathMessage(event.getEntity());

        if (Config.SEND_DEATH_MESSAGES.get())
            WebHookHelper.SendWebHook(WebHookHelper.DiscordWebHookURL, String.format("%s.", deathMessage.getString()));

    }

}
