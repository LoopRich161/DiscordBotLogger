package ru.looprich.discordlogger;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.frostdelta.discord.BotCommand;
import ru.frostdelta.discord.FakePlayerPermissionManager;
import ru.frostdelta.discord.events.*;
import ru.looprich.discordlogger.authentication.GameAuthentication;
import ru.looprich.discordlogger.deauthentication.GameDeauthentication;
import ru.looprich.discordlogger.modules.DiscordBot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class DiscordLogger extends JavaPlugin {

    private static DiscordLogger plugin;
    public DiscordBot discordBot;
    private Network network;
    public List<GameAuthentication> gameAuthenticationUsers;
    public List<GameDeauthentication> gameDeauthenticationPlayers;

    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        this.reloadConfig();
        if (getServer().getPluginManager().getPlugin("Citizens") == null || !Objects.requireNonNull(getServer().getPluginManager().getPlugin("Citizens")).isEnabled()) {
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(new AsyncPlayerChat(), this);
        getServer().getPluginManager().registerEvents(new PlayerLogin(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocess(), this);
        getServer().getPluginManager().registerEvents(new Broadcast(), this);
        getServer().getPluginManager().registerEvents(new ServerCommand(), this);
        getServer().getPluginManager().registerEvents(new RemoteServerCommand(), this);
        //getServer().getPluginManager().registerEvents(new Achievement(), this);

        boolean isEnabled = getConfig().getBoolean("bot.enabled");
        if (isEnabled) {
            checkDatabase();
            getLogger().info("DiscordBotLogging enabled!");
            getLogger().info("Loading...");
            loadDiscordBot();
            BotCommand.reg();
            gameAuthenticationUsers = new ArrayList<>();
            gameDeauthenticationPlayers = new ArrayList<>();
        } else getLogger().info("DiscordBotLogging disabled!");
        getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "Authors: " + getDescription().getAuthors());
        getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "WebSite: " + getDescription().getWebsite());

        FakePlayerPermissionManager.load();
    }

    public void loadDiscordBot() {
        String token = getConfig().getString("bot.token");
        String channel = getConfig().getString("bot.channel-id");
        discordBot = new DiscordBot(token, channel);
        if (!discordBot.createBot()) {
            getLogger().severe("PLUGIN DISABLE! YOU HAVE PROBLEMS WITH DISCORD BOT!");
            getPluginLoader().disablePlugin(this);
        } else getLogger().info("Bot v" + this.getDescription().getVersion() + " successful loaded!");
    }

    private void checkDatabase() {
        String url = getConfig().getString("network.url");
        String username = getConfig().getString("network.username");
        String password = getConfig().getString("network.password");
        network = new Network(getLogger(), url, username, password);
        if (network.init()) {
            getLogger().info("Database found!");
            network.createDB();
        } else {
            getLogger().severe("Database not found!");
            getPluginLoader().disablePlugin(this);
        }
    }

    public Network getNetwork() {
        return network;
    }

    public static DiscordLogger getInstance() {
        return plugin;
    }

    @Override
    public void onDisable() {
        getLogger().info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
        if (DiscordBot.isEnabled()) {
            DiscordBot.sendImportantMessage("Я выключился!");
            DiscordBot.shutdown();
        }
        network.close();
        this.saveConfig();
        getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "Authors: " + getDescription().getAuthors());
        getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "WebSite: " + getDescription().getWebsite());
    }
}
