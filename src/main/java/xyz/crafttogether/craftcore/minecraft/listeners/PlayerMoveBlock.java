package xyz.crafttogether.craftcore.minecraft.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.crafttogether.craftcore.minecraft.events.PlayerMoveBlockEvent;
import xyz.crafttogether.craftcore.minecraft.utils.Warmup;
import xyz.crafttogether.craftcore.minecraft.utils.WarmupHandler;

public class PlayerMoveBlock implements Listener {
    @EventHandler
    public void playerMoveBlockEvent(PlayerMoveBlockEvent event) {
        for (Warmup warmup : WarmupHandler.getCommandWarmups()) {
            if (warmup.getPlayer().getUniqueId().equals(event.getPlayer().getUniqueId())) {
                warmup.getCallback().callback(false);
                WarmupHandler.removeWarmup(warmup);
            }
        }
    }
}
