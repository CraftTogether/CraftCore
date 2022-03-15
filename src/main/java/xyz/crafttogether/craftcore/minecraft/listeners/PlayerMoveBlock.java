package xyz.crafttogether.craftcore.minecraft.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.crafttogether.craftcore.minecraft.events.PlayerMoveBlockEvent;
import xyz.crafttogether.craftcore.minecraft.utils.Warmup;
import xyz.crafttogether.craftcore.minecraft.utils.WarmupHandler;

import java.util.Iterator;

public class PlayerMoveBlock implements Listener {
    @EventHandler
    public void playerMoveBlockEvent(PlayerMoveBlockEvent event) {
        Iterator<Warmup> it = WarmupHandler.getCommandWarmups().iterator();
        while (it.hasNext()) {
            Warmup warmup = it.next();
            if (warmup.getPlayer().getUniqueId().equals(event.getPlayer().getUniqueId())) {
                warmup.getCallback().callback(false);
                it.remove();
            }
        }
    }
}
