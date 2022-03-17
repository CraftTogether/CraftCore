package xyz.crafttogether.craftcore.minecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.crafttogether.craftcore.data.DataHandler;

/**
 * Command which allows a player to set their home to their current position
 */
public class SetHomeCommand implements CommandExecutor {

    /**
     * Method invoked when the set home command is executed
     *
     * @param commandSender The command executor
     * @param command       The command executed
     * @param s             The command label
     * @param args          The command arguments
     * @return True if the command is handled successfully, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "This command must be executed by a player");
            return true;
        }
        Player p = (Player) commandSender;
        if (!p.hasPermission("craftcore.sethome")) {
            p.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            return true;
        }
        Location l = p.getLocation();
        DataHandler.setHomeLocation(p.getUniqueId(), l);
        p.sendMessage(ChatColor.GREEN + "Your home has been set at: " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ());
        return true;
    }
}
