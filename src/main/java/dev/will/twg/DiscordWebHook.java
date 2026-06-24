package dev.will.twg;

import dev.will.twg.events.PlayerEvents;
import dev.will.twg.events.ServerEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DiscordWebHook.MODID)
public class DiscordWebHook {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "discordwebhook";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    private IEventBus modEventBus;

    public static MinecraftServer Server;

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public DiscordWebHook(IEventBus modEventBus, ModContainer modContainer) {

        this.modEventBus = modEventBus;
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(new PlayerEvents());
        NeoForge.EVENT_BUS.register(new ServerEvents());

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

    }

    private void commonSetup(FMLCommonSetupEvent event) {

        LOGGER.info("DiscordWebHook Mod Loaded.");

    }

}
