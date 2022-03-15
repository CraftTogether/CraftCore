package xyz.crafttogether.craftcore.data;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.craftcore.CraftCore;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class DataHandler {
    private static final Logger logger = LoggerFactory.getLogger(DataHandler.class);
    private static File serverFile;
    private static YamlConfiguration serverConfig;

    private static File userFile;
    private static YamlConfiguration userConfig;

    public static void load() throws IOException {
        serverFile = new File(CraftCore.getPlugin().getDataFolder() + "/server.yml");
        if (serverFile.createNewFile()) {
            logger.warn("Could not find server data file, creating a new one");
        }

        userFile = new File(CraftCore.getPlugin().getDataFolder() + "/users.yml");
        if (userFile.createNewFile()) {
            logger.warn("Could not find user data file, creating one for you");
        }
        reload();
    }

    public static void reload() {
        serverConfig = YamlConfiguration.loadConfiguration(serverFile);
    }

    @Nullable
    public static String getSpawnLocation() {
        return serverConfig.getString("spawn");
    }

    public static void setSpawnLocation(Location spawnLocation) {
        serverConfig.set("spawn", spawnLocation.getBlockX() + "," + spawnLocation.getBlockY() + "," + spawnLocation.getBlockZ());
        try {
            serverConfig.save(serverFile);
        } catch (IOException e) {
            logger.error("could not save spawn location to server data file");
            e.printStackTrace();
        }
    }

    public static String getHomeLocation(UUID playerUUID) {
        return userConfig.getString(playerUUID + ".homeLocation");
    }

    public static void setHomeLocation(UUID playerUUID, Location location) {
        userConfig.set(playerUUID + ".homeLocation", location.getWorld().getName() + "," + location.getBlockX() + "," +
                location.getBlockY() + "," + location.getBlockZ());
        try {
            userConfig.save(userFile);
        } catch (IOException e) {
            logger.error("Failed to save home location to file of player with the UUID: " + playerUUID);
            e.printStackTrace();
        }
    }
}
