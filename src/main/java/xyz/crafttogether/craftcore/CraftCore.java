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

/**
 * CraftCore plugin, core library for craft together plugins, provides layer of abstraction over certain aspects of
 * plugin development
 */
public class CraftCore extends JavaPlugin {
    /**
     * Hashmap containing the verification codes for discord ids
     */
    public static final HashMap<Long, VerifyCode> verify = new HashMap<>();
    /**
     * SLF4J Logger instance
     */
    private static final Logger logger = LoggerFactory.getLogger(CraftCore.class);
    /**
     * Latest version of the config, DO NOT TOUCH
     */
    private static final int CONFIG_VERSION = 1;
    /**
     * Hashmap containing all the discord commands
     */
    private static final HashMap<String, DiscordCommand> discordCommands = new HashMap<>();
    /**
     * Static instance of the plugin
     */
    private static JavaPlugin plugin;
    /**
     * Static JDA instance
     */
    private static JDA jda;
    /**
     * Timer which is used to check if the verification code has expired
     */
    private static Timer verificationTimer;

    /**
     * Gets the plugin instace
     *
     * @return The plugin instance
     */
    public static JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Gets the current config version, used to check whether the config file is outdated
     *
     * @return The current config version
     */
    public static int getRequiredConfigVersion() {
        return CONFIG_VERSION;
    }

    /**
     * Unloads the plugin
     */
    public static void unload() {
        getPlugin().getPluginLoader().disablePlugin(getPlugin());
    }

    /**
     * Gets the static JDA instance
     *
     * @return The static JDA instance
     */
    public static JDA getJda() {
        return jda;
    }

    /**
     * Adds event listener(s) to the event list
     *
     * @param eventListeners The event listener which you would like to register
     */
    public static void addListeners(EventListener... eventListeners) {
        if (jda != null) {
            for (EventListener listener : eventListeners) {
                jda.addEventListener(listener);
            }
        }
    }

    /**
     * Adds a discord slash command and adds it to the discord commands hashmap
     *
     * @param command The command which you would like to register
     */
    public static void addDiscordCommand(DiscordCommand command) {
        getJda().upsertCommand(command.getCommandName(), command.getCommandDescription()).queue();
        discordCommands.put(command.getCommandName(), command);
    }

    /**
     * Checks whether a verification code already exists for a specific discord user
     *
     * @param discordId The id of the user
     * @return Whether that user already has a verification code which is still valid
     */
    public static boolean doesCodeAlreadyExists(long discordId) {
        return verify.containsKey(discordId);
    }

    /**
     * Adds a verification code to the HashMap containing all verification codes which are awaiting verification
     *
     * @param discordId The id of the discord user
     * @param code The VerifyCode
     */
    public static void addVerifyCode(long discordId, VerifyCode code) {
        verify.putIfAbsent(discordId, code);
    }

    /**
     * Gets the HashMap containing all the verification codes
     *
     * @return The HashMap containing all the verification codes
     */
    public static HashMap<Long, VerifyCode> getVerificationCodes() {
        return verify;
    }

    /**
     * Removes a verification code from the HashMap
     *
     * @param discordId The discorc user id
     */
    public static void removeVerificationCode(long discordId) {
        verify.remove(discordId);
    }

    /**
     * Checks whether the verification code is valid
     *
     * @param code The verification code
     * @return An optional containing the discord user id, or an empty optional if the code was invalid
     */
    public static Optional<Long> verifyCode(String code) {
        for (Map.Entry<Long, VerifyCode> entry : verify.entrySet()) {
            if (entry.getValue().getCode().equals(code)) {
                removeVerificationCode(entry.getKey());
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }

    /**
     * Gets a discord command from the discord commands HashMap
     *
     * @param commandName The name of the discord command
     * @return The DiscordCommand
     */
    @Nullable
    public static DiscordCommand getDiscordCommand(String commandName) {
        return discordCommands.getOrDefault(commandName, null);
    }

    /**
     * Invoked when the plugin is enabled
     */
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

    /**
     * Invoked when the plugin is disabled
     */
    @Override
    public void onDisable() {
        verificationTimer.cancel();
        Bukkit.getScheduler().cancelTasks(this);
        jda.shutdown();
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "CraftCore unloaded");
    }

    /**
     * Gets an account information
     *
     * @param type The account type
     * @param filter The UUID of the minecraft user or the discord user id as a string
     * @return An optional containing the AccountConnection of that user, if not found then an empty Optional is
     * returned
     */
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

    /**
     * Register bukkit events
     */
    private void registerEvents() {
        PluginManager manager = plugin.getServer().getPluginManager();
        manager.registerEvents(new PlayerMove(), this);
        manager.registerEvents(new PlayerMoveBlock(), this);
    }
}
