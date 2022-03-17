package xyz.crafttogether.craftcore.minecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.crafttogether.craftcore.data.DataHandler;
import xyz.crafttogether.craftcore.minecraft.utils.WarmupHandler;

/**
 * Command which teleports the player to spawn (will fail if there is no spawn set on the server)
 */
public class SpawnCommand implements CommandExecutor {

    /**
     * Method invoked when the spawn command is executed
     *
     * @param commandSender The command executor
     * @param command The command executed
     * @param s The command label
     * @param args The command arguments
     * @return True if the command is handled successfully, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "This command must be executed as a player");
            return true;
        }
        Player p = (Player) commandSender;
        if (!p.hasPermission("craftcore.spawn")) {
            p.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            return true;
        }
        String spawn = DataHandler.getSpawnLocation();
        if (spawn == null) {
            p.sendMessage(ChatColor.RED + "The has been no spawn set on the server");
        }
        String[] coords = spawn.split(",");
        long x = Long.parseLong(coords[0]);
        long y = Long.parseLong(coords[1]);
        long z = Long.parseLong(coords[2]);
        Location location = new Location(Bukkit.getWorld("world"), x, y, z);
        if (p.hasPermission("craftcore.spawn.ignoredelay") || p.isOp()) {
            p.teleport(location);
            p.sendMessage(ChatColor.GREEN + "You have been teleported to spawn");
        } else {
            WarmupHandler.schedule(p, 5, (successful -> {
                if (successful) {
                    p.teleport(location);
                    p.sendMessage(ChatColor.GREEN + "You have been teleported to spawn");
                } else {
                    p.sendMessage(ChatColor.RED + "You moved, teleportation cancelled");
                }
            }));
        }
        return true;
    }
}
