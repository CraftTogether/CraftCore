package xyz.crafttogether.craftcore;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.crafttogether.craftcore.configuration.ConfigHandler;
import xyz.crafttogether.craftcore.connector.AccountConnection;
import xyz.crafttogether.craftcore.connector.AccountConnector;
import xyz.crafttogether.craftcore.connector.AccountType;
import xyz.crafttogether.craftcore.discord.DiscordCommand;
import xyz.crafttogether.craftcore.discord.DiscordCommandHandler;
import xyz.crafttogether.craftcore.discord.VerifyCode;
import xyz.crafttogether.craftcore.discord.VerifyExpireTask;
import xyz.crafttogether.craftcore.discord.commands.LinkCommand;
import xyz.crafttogether.craftcore.discord.commands.UnlinkCommand;
import xyz.crafttogether.craftcore.minecraft.commands.MinecraftUnlinkCommand;
import xyz.crafttogether.craftcore.minecraft.commands.VerifyCommand;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;

public class CraftCore extends JavaPlugin {
    private static JavaPlugin plugin;
    private static JDA jda;
    private static final int CONFIG_VERSION = 1;
    private static final HashMap<String, DiscordCommand> discordCommands = new HashMap<>();
    public static final HashMap<Long, VerifyCode> verify = new HashMap<>();

    // verification checking
    private static Timer timer;

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        ConfigHandler.loadConfig();
        try {
            jda = JDABuilder.createLight(ConfigHandler.getConfig().getDiscordToken())
                    .build()
                    .awaitReady();
        } catch (InterruptedException | LoginException e) {
            e.printStackTrace();
            unload();
        }

        // Minecraft command registering
        getCommand("verify").setExecutor(new VerifyCommand());
        getCommand("unlink").setExecutor(new MinecraftUnlinkCommand());

        // command handler
        addListeners(new DiscordCommandHandler());

        // add builtin discord commands
        addDiscordCommand(new LinkCommand());
        addDiscordCommand(new UnlinkCommand());

        // verification code checking
        timer = new Timer();
        timer.scheduleAtFixedRate(new VerifyExpireTask(), 0L, ConfigHandler.getConfig().getVerifyCheckDelay());

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "CraftCore loaded");
    }

    @Override
    public void onDisable() {
        timer.cancel();
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "CraftCore unloaded");
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static int getRequiredConfigVersion() {
        return CONFIG_VERSION;
    }

    public static void unload() {
        getPlugin().getPluginLoader().disablePlugin(getPlugin());
    }

    public static JDA getJda() {
        return jda;
    }

    public static void addListeners(EventListener... eventListeners) {
        if (jda != null) {
            for (EventListener listener : eventListeners) {
                jda.addEventListener(listener);
            }
        }
    }

    public Optional<AccountConnection> getAccount(AccountType type, String filter) {
        switch (type) {
            case DISCORD -> {
                for (AccountConnection account : AccountConnector.getAccounts()) {
                    if (account.getDiscordId() == Long.parseLong(filter)) {
                        return Optional.of(account);
                    }
                }
            }

            case MINECRAFT -> {
                for (AccountConnection account : AccountConnector.getAccounts()) {
                    if (account.getMinecraftUUID().equals(filter)) {
                        return Optional.of(account);
                    }
                }
            }
        }
        return Optional.empty();
    }

    public static void addDiscordCommand(DiscordCommand command) {
        getJda().upsertCommand(command.getCommandName(), command.getCommandDescription()).queue();
        discordCommands.put(command.getCommandName(), command);
    }

    public static boolean doesCodeAlreadyExists(long discordId) {
        return verify.containsKey(discordId);
    }

    public static void addVerifyCode(long discordId, VerifyCode code) {
        verify.putIfAbsent(discordId, code);
    }

    public static HashMap<Long, VerifyCode> getVerificationCodes() {
        return verify;
    }

    public static void removeVerificationCode(long discordId) {
        verify.remove(discordId);
    }

    public static Optional<Long> verifyCode(String code) {
        for (Map.Entry<Long, VerifyCode> entry : verify.entrySet()) {
            if (entry.getValue().getCode().equals(code)) {
                removeVerificationCode(entry.getKey());
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }
}
