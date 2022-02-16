package xyz.crafttogether.craftcore.connector;

public class AccountConnection {
    private long discordId;
    private String minecraftUUID;

    public AccountConnection(long discordId, String minecraftUUID) {
        this.discordId = discordId;
        this.minecraftUUID = minecraftUUID;
    }

    public long getDiscordId() {
        return discordId;
    }

    public String getMinecraftUUID() {
        return minecraftUUID;
    }
}
