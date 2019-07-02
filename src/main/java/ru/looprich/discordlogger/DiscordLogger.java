package ru.looprich.discordlogger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.frostdelta.discord.BotCommand;
import ru.frostdelta.discord.events.*;
import ru.looprich.discordlogger.modules.DiscordBot;
import ru.looprich.discordlogger.snapping.GameSnapping;

import java.util.ArrayList;
import java.util.List;

public class DiscordLogger extends JavaPlugin {

    private static DiscordLogger plugin;
    public DiscordBot discordBot;
    private Network network;
    public List<GameSnapping> verifyUsers;

    @Override
    public void onEnable() {
        this.getLogger().info("Developed by " + getDescription().getAuthors());
        plugin = this;
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new AsyncChatEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerLoginEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessEvent(), this);
        getServer().getPluginManager().registerEvents(new BroadcastEvent(), this);
        getServer().getPluginManager().registerEvents(new AchievementEvent(), this);

        boolean isEnabled = getConfig().getBoolean("bot.enabled");
        if (isEnabled) {
            checkDatabase();
            getLogger().info("DiscordBotLogging enabled!");
            getLogger().info("Loading...");
            loadDiscordBot();
            BotCommand.reg();
            verifyUsers = new ArrayList<>();
        } else getLogger().info("DiscordBotLogging disabled!");
    }

    public void loadDiscordBot() {
        String token = getConfig().getString("bot.token");
        String channel = getConfig().getString("bot.channel-id");
        discordBot = new DiscordBot(token, channel);
        if (!discordBot.createBot()) {
            getLogger().severe("PLUGIN DISABLE! YOU HAVE PROBLEMS WITH DISCORD BOT!");
            plugin.setEnabled(false);
        } else getLogger().info("Bot successful loaded!");
    }

    private void checkDatabase() {
        String url = getConfig().getString("network.url");
        String username = getConfig().getString("network.username");
        String password = getConfig().getString("network.password");
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            network = new Network(getLogger(), url, username, password);
            if (network.init()) {
                getLogger().info("Database find!");
                network.createDB();
            } else {
                getLogger().severe("Database not find!");
                plugin.setEnabled(false);
            }

        });


    }

    public Network getNetwork() {
        return network;
    }

    public static DiscordLogger getInstance() {
        return plugin;
    }

    @Override
    public void onDisable() {
        if (DiscordBot.isEnabled()) {
            DiscordBot.sendImportantMessage("Я выключился!");
            DiscordBot.shutdown();
        }
        network.close();

    }
}
