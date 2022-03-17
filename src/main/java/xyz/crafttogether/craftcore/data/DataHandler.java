package xyz.crafttogether.craftcore.data;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.craftcore.CraftCore;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Class which abstracts the handling of the plugin data
 */
public class DataHandler {
    /**
     * The SLF4J Logger instance
     */
    private static final Logger logger = LoggerFactory.getLogger(DataHandler.class);
    /**
     * The server.yml file
     */
    private static File serverFile;
    /**
     * The server data
     */
    private static YamlConfiguration serverConfig;

    /**
     * The users.yml file
     */
    private static File userFile;
    /**
     * The users data
     */
    private static YamlConfiguration userConfig;

    /**
     * Loads the files which contain the plugin data
     *
     * @throws IOException If an error occurs while attempting to create the files
     */
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

    /**
     * Reloads the configuration from the file
     */
    public static void reload() {
        serverConfig = YamlConfiguration.loadConfiguration(serverFile);
        userConfig = YamlConfiguration.loadConfiguration(userFile);
    }

    /**
     * Gets the server spawn location
     *
     * @return The server spawn location in the format [x,y,z]
     */
    @Nullable
    public static String getSpawnLocation() {
        return serverConfig.getString("spawn");
    }

    /**
     * Sets the server spawn location
     *
     * @param spawnLocation The Location which should be set as the server spawn
     */
    public static void setSpawnLocation(Location spawnLocation) {
        serverConfig.set("spawn", spawnLocation.getBlockX() + "," + spawnLocation.getBlockY() + "," + spawnLocation.getBlockZ());
        try {
            serverConfig.save(serverFile);
        } catch (IOException e) {
            logger.error("could not save spawn location to server data file");
            e.printStackTrace();
        }
    }

    /**
     * Gets the location of a players home
     *
     * @param playerUUID The UUID of the player whose home location you are fetching
     * @return The location of the players home in the format [world,x,y,z]
     */
    @Nullable
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
