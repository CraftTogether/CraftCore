package xyz.crafttogether.craftcore.minecraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.crafttogether.craftcore.minecraft.events.PlayerMoveBlockEvent;
import xyz.crafttogether.craftcore.minecraft.utils.Warmup;
import xyz.crafttogether.craftcore.minecraft.utils.WarmupHandler;

public class PlayerMove implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPlayerMoveEvent(PlayerMoveEvent event) {
        Location al = event.getTo();
        Location bf = event.getFrom();
        if (al.getBlockX() - bf.getBlockX() != 0 || al.getBlockY() - bf.getBlockY() != 0 || al.getBlockZ() - bf.getBlockZ() != 0) {
            Bukkit.getPluginManager().callEvent(new PlayerMoveBlockEvent(event.getPlayer(), event.getPlayer().getLocation()));
        }
        if (!event.isCancelled()) {
            for (Warmup warmup : WarmupHandler.getCommandWarmups()) {
                if (event.getPlayer().getUniqueId().equals(warmup.getPlayerUUID())) {
                    warmup.getCallback().callback(false);
                }
            }
        }
    }
}
