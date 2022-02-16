package xyz.crafttogether.craftcore;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.crafttogether.craftcore.configuration.ConfigHandler;

import javax.security.auth.login.LoginException;

public class CraftCore extends JavaPlugin {
    private static JavaPlugin plugin;
    private static JDA jda;
    private static final int CONFIG_VERSION = 1;

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        try {
            jda = JDABuilder.createLight(ConfigHandler.getConfig().getDiscordToken())
                    .build()
                    .awaitReady();
        } catch (InterruptedException | LoginException e) {
            e.printStackTrace();
            unload();
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "CraftCore loaded");
    }

    @Override
    public void onDisable() {
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
}
