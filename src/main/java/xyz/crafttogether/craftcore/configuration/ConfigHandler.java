package xyz.crafttogether.craftcore.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.craftcore.CraftCore;

import java.io.File;

public class ConfigHandler {
    private static final Logger logger = LoggerFactory.getLogger(ConfigHandler.class);
    private static File file;
    private static Config config;

    public static void loadConfig() {
        file = new File(CraftCore.getPlugin().getDataFolder() + "/config.yml");
        reloadConfig();
    }

    public static void reloadConfig() {
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        config = new Config(
                fc.getInt("configVersion"),
                fc.getString("discordToken"),
                fc.getLong("verifyExpireDelay"),
                fc.getLong("verifyCheckDelay"),
                fc.getLong("discordGuildId"),
                fc.getLong("discordChannelId"),
                fc.getString("discordWebhook"),
                fc.getBoolean("ircEnabled"),
                fc.getString("ircUsername"),
                fc.getString("ircHostname"),
                fc.getInt("ircPort"),
                fc.getBoolean("ircUseTls"),
                fc.getInt("ircTimeout"),
                "#" + fc.getString("ircChannel"),
                fc.getInt("ircReconnectAttempts"),
                fc.getInt("ircReconnectDelay"),
                fc.getString("ircCommandPrefix"),
                fc.getString("minecraftPrefix"),
                fc.getString("ircPrefix"),
                fc.getString("serverSpawn")
        );
    }

    private static void verifyConfigVersion() {
        if (CraftCore.getRequiredConfigVersion() == config.getConfigVersion()) return;
        logger.warn("Config version outdated, generating new config");
        file.delete();
        CraftCore.getPlugin().getConfig().options().copyDefaults();
        CraftCore.getPlugin().saveDefaultConfig();
        CraftCore.unload();
    }

    public static Config getConfig() {
        return config;
    }
}
