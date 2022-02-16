package xyz.crafttogether.craftcore.minecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.crafttogether.craftcore.CraftCore;
import xyz.crafttogether.craftcore.connector.AccountConnector;

import java.util.Optional;

public class VerifyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "You must run this command as a player");
            return true;
        }
        final Player player = (Player) commandSender;
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /verify <code>");
            return true;
        }

        if (AccountConnector.containsAccount(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You have already linked your discord account, use /unlink");
        }
        Optional<Long> optional = CraftCore.verifyCode(args[0]);
        if (optional.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Invalid or expired verification code");
            return true;
        }
        long discordId = optional.get();
        AccountConnector.addAccount(discordId, player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "Successfully connected your minecraft account to discord");
        return true;
    }
}
