package xyz.crafttogether.craftcore.data;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.craftcore.CraftCore;

import java.io.File;
import java.io.IOException;

public class DataHandler {
    private static final Logger logger = LoggerFactory.getLogger(DataHandler.class);
    private static File file;
    private static YamlConfiguration config;

    public static void load() throws IOException {
        file = new File(CraftCore.getPlugin().getDataFolder() + "/server.yml");
        if (file.createNewFile()) {
            logger.warn("Could not find server data file, creating a new one");
        }
        reload();
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    @Nullable
    public static String getSpawnLocation() {
        return config.getString("spawn");
    }

    public static void setSpawnLocation(Location spawnLocation) {
        config.set("spawn", spawnLocation.getBlockX() + "," +  spawnLocation.getBlockY() + "," + spawnLocation.getBlockZ());
        try {
            config.save(file);
        } catch (IOException e) {
            logger.error("could not save spawn location to server data file");
            e.printStackTrace();
        }
    }
}
