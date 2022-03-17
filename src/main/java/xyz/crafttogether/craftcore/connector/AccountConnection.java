package xyz.crafttogether.craftcore.connector;

/**
 * A class which encapsulates an account connections data
 */
public class AccountConnection {
    /**
     * The discord id of the user
     */
    private final long discordId;
    /**
     * The minecraft UUID of the player
     */
    private final String minecraftUUID;

    /**
     * Constructor to create AccountConnection object
     *
     * @param discordId The discord id of the user
     * @param minecraftUUID The minecraft UUID of the player
     */
    public AccountConnection(long discordId, String minecraftUUID) {
        this.discordId = discordId;
        this.minecraftUUID = minecraftUUID;
    }

    /**
     * Gets the discord id of the user
     *
     * @return The discord id of the user
     */
    public long getDiscordId() {
        return discordId;
    }

    /**
     * Gets the minecraft UUID of the player
     *
     * @return Gets the minecraft UUID of the player
     */
    public String getMinecraftUUID() {
        return minecraftUUID;
    }
}
