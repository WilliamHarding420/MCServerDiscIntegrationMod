package dev.will.twg;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
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

    public static final ModConfigSpec.ConfigValue<Boolean> SEND_ADVANCEMENT_MESSAGES = BUILDER
            .comment("Whether or not to send advancement messages")
            .translation("DiscordWebHook.configuration.send_advancement_messages")
            .define("send_advancement_messages", true);

    public static final ModConfigSpec.ConfigValue<Boolean> SEND_DEATH_MESSAGES = BUILDER
            .comment("Whether or not to send death messages.")
            .translation("DiscordWebHook.configuration.send_death_messages")
            .define("send_death_messages", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    // util methods for commands, etc.
    public static Dictionary<String, ModConfigSpec.ConfigValue<?>> configOptionDictionary = null;
    public static List<String> configPaths = null;
    public static <T> ModConfigSpec.ConfigValue<?> configNameToVariable(String name) {

        if (configOptionDictionary == null || configPaths == null)
            cacheConfigPaths();

        return configOptionDictionary.get(name);

    }

    public static void cacheConfigPaths() {
        configOptionDictionary = new Hashtable<>();
        configPaths = new ArrayList<>();

        Class<Config> configClass = Config.class;
        Field[] configFields = configClass.getFields();

        for (Field configField : configFields) {
            ModConfigSpec.ConfigValue<?> field;

            try {
                field = (ModConfigSpec.ConfigValue<?>) configField.get(null);
                if (!(field instanceof ModConfigSpec.ConfigValue<?>))
                    continue;
            } catch (Exception e) {
                DiscordWebHook.LOGGER.debug("Exception occurred while caching config paths. {}", e.getMessage());
                continue;
            }

            configOptionDictionary.put(field.getPath().getFirst(), field);
            configPaths.add(field.getPath().getFirst());

        }
    }

}
