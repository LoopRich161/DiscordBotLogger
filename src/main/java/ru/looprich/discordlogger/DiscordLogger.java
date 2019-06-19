package ru.looprich.discordlogger;

import org.bukkit.plugin.java.JavaPlugin;
import ru.frostdelta.discord.BotCommand;
import ru.frostdelta.discord.events.*;
import ru.looprich.discordlogger.modules.DiscordBot;

import java.util.logging.Logger;

public class DiscordLogger extends JavaPlugin {

    private static DiscordLogger plugin;
    private boolean isEnabled;
    public DiscordBot discordBot;

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
        getServer().getPluginManager().registerEvents(new BroadcastEvent(),this);
        //getServer().getPluginManager().registerEvents(new AchievementEvent(), this);

        isEnabled = getConfig().getBoolean("bot.enabled");
        if (isEnabled) {
            getLogger().info("DiscordBotLogging enabled!");
            getLogger().info("Loading...");
            loadDiscordBot();
            BotCommand.reg();
            DiscordBot.sendImportantMessage("Я включился!");
        } else getLogger().info("DiscordBotLogging disabled!");
    }

    public void loadDiscordBot() {
        String token = getConfig().getString("bot.token");
        String channel = getConfig().getString("bot.channel-id");
        discordBot = new DiscordBot(token, channel);
        getLogger().info("Bot successful loaded!");
        if (!discordBot.createBot()) {
            getLogger().warning("PLUGIN DISABLE! YOU HAVE PROBLEMS WITH DISCORD BOT!");
            getPluginLoader().disablePlugin(this);
        }
    }

    public static DiscordLogger getInstance() {
        return plugin;
    }

    @Override
    public void onDisable() {
        if (DiscordBot.isEnabled()){
            DiscordBot.sendImportantMessage("Я выключился!");
            DiscordBot.shutdown();
        }

    }
}
