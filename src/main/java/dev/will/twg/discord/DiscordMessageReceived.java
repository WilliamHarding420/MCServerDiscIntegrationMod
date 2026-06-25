package dev.will.twg.discord;

import dev.will.twg.Config;
import dev.will.twg.DiscordWebHook;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DiscordMessageReceived extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        DiscordWebHook.LOGGER.debug("Executing message received event.");

        if (event.getAuthor().isBot()
                || event.isWebhookMessage()
                || !Config.DISCORD_BOT_CHANNEL_IDS.get().contains(event.getChannel().getId())
                || !Config.SEND_DISCORD_MESSAGES.get())
            return;

        Member msgSender = event.getGuild().retrieveMemberById(event.getAuthor().getIdLong()).complete();

        String chatMessage = String.format("[#%s] %s: %s",
                event.getChannel().getName(),
                msgSender.getNickname(),
                event.getMessage().getContentDisplay());

        MinecraftServer server = DiscordWebHook.Server;

        MutableComponent message = Component.literal(chatMessage);
        ClientboundSystemChatPacket packet = new ClientboundSystemChatPacket(message, false);

        server.sendSystemMessage(message);
        DiscordWebHook.LOGGER.debug("Broadcasting chat packet to players.");
        server.getPlayerList().broadcastAll(packet);

    }
}
