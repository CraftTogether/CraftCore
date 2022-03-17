package xyz.crafttogether.craftcore.minecraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event which is invoked when a players xyz position changes
 */
public class PlayerMoveBlockEvent extends Event {
    /**
     * The list of handlers
     */
    private static final HandlerList HANDLERS = new HandlerList();
    /**
     * The player whose position has changed
     */
    private final Player player;

    /**
     * Event constructor
     *
     * @param player The player whose position has changed
     */
    public PlayerMoveBlockEvent(Player player) {
        this.player = player;
    }

    /**
     * Static method which gets the HandlerList
     *
     * @return The HandlerList
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Gets the player whose position has changed
     *
     * @return The player whose position has changed
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the HandlerList
     *
     * @return The HandlerList
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
