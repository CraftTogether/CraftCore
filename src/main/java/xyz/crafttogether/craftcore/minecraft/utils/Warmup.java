package xyz.crafttogether.craftcore.minecraft.utils;

import java.util.UUID;

public class Warmup {
    private UUID playerUUID;
    private final int warmup;
    private final long scheduledTime;
    private final WarmupCallback callback;

    public Warmup(UUID playerUUID, int warmup, WarmupCallback callback, long scheduledTime) {
        this.playerUUID = playerUUID;
        this.warmup = warmup;
        this.callback = callback;
        this.scheduledTime = scheduledTime;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public int getWarmup() {
        return warmup;
    }

    public WarmupCallback getCallback() {
        return callback;
    }

    public long getScheduledTime() {
        return scheduledTime;
    }
}
