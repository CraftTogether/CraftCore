package xyz.crafttogether.craftcore.minecraft.utils;

import org.bukkit.entity.Player;

/**
 * Warmup class which encapsulates the warmup data
 */
public class Warmup {
    /**
     * The warmup period in seconds
     */
    private final int warmup;
    /**
     * The time which the warmup was scheduled, in seconds
     */
    private final long scheduledTime;
    /**
     * The callback for the warmup
     */
    private final WarmupCallback callback;
    /**
     * The player which invoked the command which is using the warmup
     */
    private final Player player;

    /**
     * Constructor to create Warmup object
     *
     * @param player The player which invoked the command which is using the warmup
     * @param warmup The warmup period in seconds
     * @param callback The warmup callback
     * @param scheduledTime The time that the warmup was scheduled
     */
    public Warmup(Player player, int warmup, WarmupCallback callback, long scheduledTime) {
        this.player = player;
        this.warmup = warmup;
        this.callback = callback;
        this.scheduledTime = scheduledTime;
    }

    /**
     * Gets the player which invoked the command which is using the warmup
     *
     * @return The player which invoked the command which is using the warmup
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the warmup period
     *
     * @return The warmup period in seconds
     */
    public int getWarmup() {
        return warmup;
    }

    /**
     * Gets the warmup callback
     *
     * @return The warmup callback
     */
    public WarmupCallback getCallback() {
        return callback;
    }

    /**
     * Gets the time in which the warmup was scheduled
     *
     * @return The time in which the warmup was scheduled in seconds
     */
    public long getScheduledTime() {
        return scheduledTime;
    }
}
