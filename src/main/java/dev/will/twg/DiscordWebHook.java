package dev.will.twg;

import dev.will.twg.discord.DiscordBot;
import dev.will.twg.discord.DiscordMessageReceived;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DiscordWebHook.MODID)
public class DiscordWebHook {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "discordwebhook";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    private IEventBus modEventBus;

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public DiscordWebHook(IEventBus modEventBus, ModContainer modContainer) {

        this.modEventBus = modEventBus;
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

    }

    private void commonSetup(FMLCommonSetupEvent event) {

        LOGGER.info("DiscordWebHook Mod Loaded.");

    }

    public static MinecraftServer Server;

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

        WebHookHelper.DiscordWebHookURL = Config.DISCORD_WEB_HOOK.get();
        DiscordMessageReceived.ChannelIDs = Config.DISCORD_BOT_CHANNEL_IDS.get();
        LOGGER.debug("Read discord webhook and channel IDs from config.");

        Server = event.getServer();

        NeoForge.EVENT_BUS.addListener(this::onPlayerJoin);
        NeoForge.EVENT_BUS.addListener(this::onPlayerLeave);
        NeoForge.EVENT_BUS.addListener(this::onPlayerChat);
        LOGGER.debug("Added event listeners.");

        DiscordBot.CreateBot();

        if (Config.SEND_SERVER_STATUS.get())
            WebHookHelper.SendWebHook(WebHookHelper.DiscordWebHookURL, "Server has started.");

    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        DiscordBot.DiscordBot.shutdownNow();

        if (Config.SEND_SERVER_STATUS.get())
            WebHookHelper.SendWebHook(WebHookHelper.DiscordWebHookURL, "Server shutting down.");
    }

    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {

        LOGGER.debug("Executing player join event.");

        Player player = event.getEntity();

        String webHookMessage = String.format("%s joined.", player.getDisplayName().getString());

        DiscordBot.UpdateBotStatusPlayerCount(Server.getPlayerCount());

        if (Config.SEND_PLAYER_CONNECTIONS.get())
            WebHookHelper.SendWebHook(WebHookHelper.DiscordWebHookURL, webHookMessage);

    }

    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {

        LOGGER.debug("Executing player left event.");

        Player player = event.getEntity();

        String webHookMessage = String.format("%s left.", player.getDisplayName().getString());

        DiscordBot.UpdateBotStatusPlayerCount(Server.getPlayerCount() - 1);

        if (Config.SEND_PLAYER_CONNECTIONS.get())
            WebHookHelper.SendWebHook(WebHookHelper.DiscordWebHookURL, webHookMessage);

    }

    public void onPlayerChat(ServerChatEvent event) {

        LOGGER.debug("Executing player chat event.");

        ServerPlayer player = event.getPlayer();

        String webHookMessage = String.format("%s: %s", player.getDisplayName().getString(), event.getMessage().getString());

        if(Config.SEND_MINECRAFT_MESSAGES.get())
            WebHookHelper.SendWebHook(WebHookHelper.DiscordWebHookURL, webHookMessage);

    }

}
