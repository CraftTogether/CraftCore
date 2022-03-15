package xyz.crafttogether.craftcore.minecraft.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarmupHandler {
    private static final List<Warmup> commandWarmups = new ArrayList<>();

    public static void schedule(Player player, int warmup, WarmupCallback warmupCallback) {
        synchronized (commandWarmups) {
            commandWarmups.add(new Warmup(player, warmup, warmupCallback, System.currentTimeMillis() / 1000));
        }
        player.sendMessage(ChatColor.GREEN + "You will be teleported in " + warmup + " seconds, do NOT move");
    }

    public static synchronized List<Warmup> getCommandWarmups() {
        synchronized (commandWarmups) {
            return commandWarmups;
        }
    }

    public static void removeWarmup(Warmup warmup) {
        synchronized (commandWarmups) {
            commandWarmups.remove(warmup);
        }
    }
}
