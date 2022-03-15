package xyz.crafttogether.craftcore.minecraft.commands;

import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.crafttogether.craftcore.configuration.ConfigHandler;
import xyz.crafttogether.craftcore.minecraft.utils.WarmupHandler;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof User)) {
            commandSender.sendMessage(ChatColor.RED + "This command must be executed as a player");
            return true;
        }
        Player p = (Player) commandSender;
        String spawn = ConfigHandler.getConfig().getServerSpawn();
        String[] coords = spawn.split(",");
        long x = Long.parseLong(coords[0]);
        long y = Long.parseLong(coords[1]);
        long z = Long.parseLong(coords[2]);
        Location location = new Location(Bukkit.getWorld("world"), x, y, z);
        WarmupHandler.schedule(p, 5, (successful -> {
            if (successful) {
                p.teleport(location);
                p.sendMessage(ChatColor.GREEN + "You have been teleported to spawn");
            } else {
                p.sendMessage(ChatColor.RED + "You moved, teleportation cancelled");
            }
        }));
        return true;
    }
}
