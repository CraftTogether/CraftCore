package xyz.crafttogether.craftcore.minecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.crafttogether.craftcore.connector.AccountConnector;

public class UnlinkCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "You must run this command as a player");
            return true;
        }

        final Player player = (Player) commandSender;
        if (!AccountConnector.containsAccount(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You have not linked your discord account to minecraft, use /link");
            return true;
        }
        AccountConnector.removeAccount(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "Successfully unlinked your discord account from your minecraft account");
        return true;
    }
}
