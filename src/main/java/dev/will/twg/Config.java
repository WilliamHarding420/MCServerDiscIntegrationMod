package dev.will.twg;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
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

    static final ModConfigSpec SPEC = BUILDER.build();
}
