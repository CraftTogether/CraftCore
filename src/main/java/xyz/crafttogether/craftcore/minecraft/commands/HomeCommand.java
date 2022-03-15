package xyz.crafttogether.craftcore.minecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.crafttogether.craftcore.data.DataHandler;
import xyz.crafttogether.craftcore.minecraft.utils.WarmupHandler;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "This command must be executed by a player");
        }
        Player p = (Player) commandSender;
        if (!p.hasPermission("craftcore.home")) {
            p.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            return true;
        }
        String home = DataHandler.getHomeLocation(p.getUniqueId());
        if (home == null) {
            p.sendMessage(ChatColor.RED + "You have not set a home");
            return true;
        }
        String[] coords = home.split(",");
        World world = Bukkit.getWorld(coords[0]);
        long x = Long.parseLong(coords[1]);
        long y = Long.parseLong(coords[2]);
        long z = Long.parseLong(coords[3]);
        Location location = new Location(world, x, y, z);
        if (p.hasPermission("craftcore.home.skipwarmup") || p.isOp()) {
            p.teleport(location);
            p.sendMessage(ChatColor.GREEN + "You have been teleported home");
        } else {
            WarmupHandler.schedule(p, 5, (successful -> {
                if (successful) {
                    p.teleport(location);
                    p.sendMessage(ChatColor.GREEN+ "You have been teleported home");
                } else {
                    p.sendMessage(ChatColor.RED + "You moved, teleportation cancelled");
                }
            }));
        }
        return true;
    }
}
