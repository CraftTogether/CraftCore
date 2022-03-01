package xyz.crafttogether.craftcore.minecraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerMoveBlockEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;

    public PlayerMoveBlockEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
