package xyz.crafttogether.craftcore.minecraft.utils;

import org.bukkit.entity.Player;

public class Warmup {
    private Player player;
    private final int warmup;
    private final long scheduledTime;
    private final WarmupCallback callback;

    public Warmup(Player player, int warmup, WarmupCallback callback, long scheduledTime) {
        this.player = player;
        this.warmup = warmup;
        this.callback = callback;
        this.scheduledTime = scheduledTime;
    }

    public Player getPlayer() {
        return player;
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
