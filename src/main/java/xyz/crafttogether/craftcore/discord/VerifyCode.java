package xyz.crafttogether.craftcore.discord;

import xyz.crafttogether.craftcore.configuration.ConfigHandler;

/**
 * Class which encapsulates the verification code data
 */
public class VerifyCode {
    /**
     * The verification code
     */
    private final String code;
    /**
     * The time the verification code was created, in unix millis
     */
    private final long timeCreated;

    /**
     * Constructor to create VerifyCode object
     *
     * @param code The verification code
     * @param timeCreated The time the verification code was created, in unix millis
     */
    public VerifyCode(String code, long timeCreated) {
        this.code = code;
        this.timeCreated = timeCreated;
    }

    /**
     * Gets the verification code
     *
     * @return The verification code
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the time the verification code was created
     *
     * @return The time the verification code was created, in unix millis
     */
    public long getTimeCreated() {
        return timeCreated;
    }

    /**
     * Checks whether the verification code has expired
     *
     * @return Whether the verification code has expired
     */
    public boolean hasExpired() {
        return timeCreated + ConfigHandler.getConfig().getVerifyExpireDelay() <= System.currentTimeMillis();
    }
}
