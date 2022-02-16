package xyz.crafttogether.craftcore.discord;

import xyz.crafttogether.craftcore.configuration.ConfigHandler;

public class VerifyCode {
    private String code;
    private long timeCreated;

    public VerifyCode(String code, long timeCreated) {
        this.code = code;
        this.timeCreated = timeCreated;
    }

    public String getCode() {
        return code;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public boolean hasExpired() {
        return timeCreated + ConfigHandler.getConfig().getVerifyExpireDelay() <= System.currentTimeMillis();
    }
}
