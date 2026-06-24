package dev.will.twg;

import java.util.ArrayList;
import java.util.List;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> DISCORD_BOT_TOKEN = BUILDER
            .comment("The token of your discord bot")
            .translation("DiscordWebHook.configuration.discord_bot_token")
            .define("discord_bot_token", "");

    public static final ModConfigSpec.ConfigValue<String> DISCORD_WEB_HOOK = BUILDER
            .comment("The webhook to send messages to")
            .translation("DiscordWebHook.configuration.discord_webhook")
            .define("discord_web_hook", "");

    public static final ModConfigSpec.ConfigValue<List<String>> DISCORD_BOT_CHANNEL_IDS = BUILDER
            .comment("A list of channel IDs to get the chat messages from.")
            .translation("DiscordWebHook.configuration.discord_channel_ids")
            .define("discord_channels", new ArrayList<String>());

    public static final ModConfigSpec.ConfigValue<Boolean> SEND_SERVER_STATUS = BUILDER
            .comment("Whether or not to send server starting/stopping messages")
            .translation("DiscordWebHook.configuration.send_server_status")
            .define("send_server_status", true);

    public static final ModConfigSpec.ConfigValue<Boolean> SEND_PLAYER_CONNECTIONS = BUILDER
            .comment("Whether or not to send player join/leave messages")
            .translation("DiscordWebHook.configuration.send_player_connections")
            .define("send_player_connections", true);

    public static final ModConfigSpec.ConfigValue<Boolean> SEND_MINECRAFT_MESSAGES = BUILDER
            .comment("Whether or not to send Minecraft chat messages to webhook")
            .translation("DiscordWebHook.configuration.send_minecraft_chat")
            .define("send_minecraft_chat", true);

    public static final ModConfigSpec.ConfigValue<Boolean> SEND_DISCORD_MESSAGES = BUILDER
            .comment("Whether or not to send Discord messages in the Minecraft chat")
            .translation("DiscordWebHook.configuration.send_discord_chat")
            .define("send_discord_chat", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
