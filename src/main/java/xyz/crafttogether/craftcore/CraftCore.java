package xyz.crafttogether.craftcore;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.craftcore.configuration.ConfigHandler;
import xyz.crafttogether.craftcore.connector.AccountConnection;
import xyz.crafttogether.craftcore.connector.AccountConnector;
import xyz.crafttogether.craftcore.connector.AccountType;
import xyz.crafttogether.craftcore.data.DataHandler;
import xyz.crafttogether.craftcore.discord.DiscordCommand;
import xyz.crafttogether.craftcore.discord.DiscordCommandHandler;
import xyz.crafttogether.craftcore.discord.VerifyCode;
import xyz.crafttogether.craftcore.discord.VerifyExpireTask;
import xyz.crafttogether.craftcore.discord.commands.LinkCommand;
import xyz.crafttogether.craftcore.discord.commands.UnlinkCommand;
import xyz.crafttogether.craftcore.minecraft.commands.*;
import xyz.crafttogether.craftcore.minecraft.listeners.PlayerMove;
import xyz.crafttogether.craftcore.minecraft.listeners.PlayerMoveBlock;
import xyz.crafttogether.craftcore.minecraft.utils.Warmup;
import xyz.crafttogether.craftcore.minecraft.utils.WarmupHandler;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.*;

public class CraftCore extends JavaPlugin {
    public static final HashMap<Long, VerifyCode> verify = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(CraftCore.class);
    private static final int CONFIG_VERSION = 1;
    private static final HashMap<String, DiscordCommand> discordCommands = new HashMap<>();
    private static JavaPlugin plugin;
    private static JDA jda;
    // verification checking
    private static Timer verificationTimer;

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

    @Nullable
    public static DiscordCommand getDiscordCommand(String commandName) {
        return discordCommands.getOrDefault(commandName, null);
    }

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        ConfigHandler.loadConfig();
        try {
            DataHandler.load();
        } catch (IOException e) {
            logger.error("Failed to load data");
            e.printStackTrace();
        }
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
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("home").setExecutor(new HomeCommand());
        getCommand("sethome").setExecutor(new SetHomeCommand());
        registerEvents();

        // command handler
        addListeners(new DiscordCommandHandler());

        // add builtin discord commands
        addDiscordCommand(new LinkCommand());
        addDiscordCommand(new UnlinkCommand());

        // verification code checking
        verificationTimer = new Timer();
        verificationTimer.scheduleAtFixedRate(new VerifyExpireTask(), 0L, ConfigHandler.getConfig().getVerifyCheckDelay());

        Bukkit.getScheduler().runTaskTimer(this, new TimerTask() {
            @Override
            public void run() {
                Iterator<Warmup> it = WarmupHandler.getCommandWarmups().iterator();
                while (it.hasNext()) {
                    Warmup warmup = it.next();
                    if (warmup.getWarmup() + warmup.getScheduledTime() < System.currentTimeMillis() / 1000) {
                        warmup.getCallback().callback(true);
                        it.remove();
                    }
                }
            }
        }, 0, 20);

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "CraftCore loaded");
    }

    @Override
    public void onDisable() {
        verificationTimer.cancel();
        Bukkit.getScheduler().cancelTasks(this);
        jda.shutdown();
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "CraftCore unloaded");
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

    private void registerEvents() {
        PluginManager manager = plugin.getServer().getPluginManager();
        manager.registerEvents(new PlayerMove(), this);
        manager.registerEvents(new PlayerMoveBlock(), this);
    }
}
