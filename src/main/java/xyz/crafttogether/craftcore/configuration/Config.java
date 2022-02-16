package xyz.crafttogether.craftcore.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.craftcore.CraftCore;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private final int configVersion;
    private final String discordToken;

    public Config(int configVersion, String discordToken) {
        try {
            assert (discordToken != null);
        } catch (AssertionError e) {
            logger.error("Invalid CraftCore config");
            CraftCore.unload();
        }
        this.configVersion = configVersion;
        this.discordToken = discordToken;
    }

    public int getConfigVersion() {
        return configVersion;
    }

    public String getDiscordToken() {
        return discordToken;
    }
}
