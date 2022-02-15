package xyz.crafttogether.craftcore.configuration;

public class Config {
    private final int configVersion;
    private final String discordToken;

    public Config(int configVersion, String discordToken) {
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
