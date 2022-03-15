package xyz.crafttogether.craftcore.minecraft.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the warmups which have been scheduled
 */
public class WarmupHandler {
    /**
     * SLF4J Logger instance
     */
    private static final List<Warmup> commandWarmups = new ArrayList<>();

    /**
     * Schedule a new warmup, the callback will be called when either the warmup is cancelled (the player has moved)
     * and successful will be false, or the callback will be called when the warmup period is over, and successful
     * will be true.
     *
     * @param player The player which invoked the command which has a warmup
     * @param warmup The time in seconds which the warmup lasts
     * @param warmupCallback The warmup callback which will be executed when the player either moves or the warmup
     *                       period is over
     */
    public static void schedule(Player player, int warmup, WarmupCallback warmupCallback) {
        commandWarmups.add(new Warmup(player, warmup, warmupCallback, System.currentTimeMillis() / 1000));
        player.sendMessage(ChatColor.GREEN + "You will be teleported in " + warmup + " seconds, do NOT move");
    }

    /**
     * Get the list of current warmups
     *
     * @return
     */
    public static List<Warmup> getCommandWarmups() {
        return commandWarmups;
    }

    /**
     * Removes a warmup from the list
     *
     * @param warmup The warmup you would like to remove
     */
    public static void removeWarmup(Warmup warmup) {
        commandWarmups.remove(warmup);
    }
}
