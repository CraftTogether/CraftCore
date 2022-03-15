package xyz.crafttogether.craftcore.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.craftcore.CraftCore;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    // CraftCore configuration
    private final int configVersion;
    private final String discordToken;
    private final long verifyExpireDelay;
    private final long verifyCheckDelay;

    // ChatBridge configuration
    private final long discordGuildId;
    private final long discordChannelId;
    private final String discordWebhook;
    private final boolean ircEnabled;
    private final String ircUsername;
    private final String ircHostname;
    private final int ircPort;
    private final boolean ircUseTls;
    private final int ircTimeout;
    private final String ircChannel;
    private final int ircReconnectAttempts;
    private final int ircReconnectDelay;
    private final String ircCommandPrefix;
    private final String minecraftPrefix;
    private final String ircPrefix;
    private final String serverSpawn;

    public Config(int configVersion, String discordToken, long verifyExpireDelay, long verifyCheckDelay,
                  long discordGuildId, long discordChannelId, String discordWebhook, boolean ircEnabled,
                  String ircUsername, String ircHostname, int ircPort, boolean ircUseTls, int ircTimeout,
                  String ircChannel, int ircReconnectAttempts, int ircReconnectDelay, String ircCommandPrefix,
                  String minecraftPrefix, String ircPrefix, String serverSpawn) {
        try {
            // CraftCore
            assert (discordToken != null);

            // ChatBridge
            assert (discordWebhook != null);
            assert (ircUsername != null);
            assert (ircHostname != null);
            assert (ircChannel != null);
            assert (ircCommandPrefix != null);
            assert (minecraftPrefix != null);
            assert (ircPrefix != null);
            assert (serverSpawn != null);
        } catch (AssertionError e) {
            logger.error("Invalid CraftCore config");
            CraftCore.unload();
        }
        // CraftCore
        this.configVersion = configVersion;
        this.discordToken = discordToken;
        this.verifyExpireDelay = verifyExpireDelay * 1000;
        this.verifyCheckDelay = verifyCheckDelay * 1000;
        this.serverSpawn = serverSpawn;

        // Chat Bridge
        this.discordGuildId = discordGuildId;
        this.discordChannelId = discordChannelId;
        this.discordWebhook = discordWebhook;
        this.ircEnabled = ircEnabled;
        this.ircUsername = ircUsername;
        this.ircHostname = ircHostname;
        this.ircPort = ircPort;
        this.ircUseTls = ircUseTls;
        this.ircTimeout = ircTimeout;
        this.ircChannel = ircChannel;
        this.ircReconnectAttempts = ircReconnectAttempts;
        this.ircReconnectDelay = ircReconnectDelay;
        this.ircCommandPrefix = ircCommandPrefix;
        this.minecraftPrefix = minecraftPrefix;
        this.ircPrefix = ircPrefix;
    }

    public int getConfigVersion() {
        return configVersion;
    }

    public String getDiscordToken() {
        return discordToken;
    }

    public long getVerifyExpireDelay() {
        return verifyExpireDelay;
    }

    public long getVerifyCheckDelay() {
        return verifyCheckDelay;
    }

    public long getDiscordGuildId() {
        return discordGuildId;
    }

    public long getDiscordChannelId() {
        return discordChannelId;
    }

    public String getDiscordWebhook() {
        return discordWebhook;
    }

    public boolean isIrcEnabled() {
        return ircEnabled;
    }

    public String getIrcUsername() {
        return ircUsername;
    }

    public String getIrcHostname() {
        return ircHostname;
    }

    public int getIrcPort() {
        return ircPort;
    }

    public boolean isIrcUseTls() {
        return ircUseTls;
    }

    public int getIrcTimeout() {
        return ircTimeout;
    }

    public String getIrcChannel() {
        return ircChannel;
    }

    public int getIrcReconnectAttempts() {
        return ircReconnectAttempts;
    }

    public int getIrcReconnectDelay() {
        return ircReconnectDelay;
    }

    public String getIrcCommandPrefix() {
        return ircCommandPrefix;
    }

    public String getMinecraftPrefix() {
        return minecraftPrefix;
    }

    public String getIrcPrefix() {
        return ircPrefix;
    }

    public String getServerSpawn() {
        return serverSpawn;
    }
}
