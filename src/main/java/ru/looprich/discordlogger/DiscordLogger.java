package ru.looprich.discordlogger;

import org.bukkit.plugin.java.JavaPlugin;
import ru.frostdelta.discord.BotCommand;
import ru.frostdelta.discord.events.*;
import ru.looprich.discordlogger.modules.DiscordBot;

public class DiscordLogger extends JavaPlugin {

    private static DiscordLogger plugin;
    public DiscordBot discordBot;
    private Network network;

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

        boolean isEnabled = getConfig().getBoolean("bot.enabled");
        if (isEnabled) {
            getLogger().info("DiscordBotLogging enabled!");
            getLogger().info("Loading...");
            loadDiscordBot();
            BotCommand.reg();
        } else getLogger().info("DiscordBotLogging disabled!");
    }

    public void loadDiscordBot() {
        String token = getConfig().getString("bot.token");
        String channel = getConfig().getString("bot.channel-id");
        if (token == null || channel == null) {
            getLogger().severe("Token or channel is null!");
        }
        discordBot = new DiscordBot(token, channel);
        if (!discordBot.createBot()) {
            getLogger().warning("PLUGIN DISABLE! YOU HAVE PROBLEMS WITH DISCORD BOT!");
            getPluginLoader().disablePlugin(this);
        } else getLogger().info("Bot successful loaded!");
    }

    private void checkDatabase() {
        String url = getConfig().getString("network.url");
        String username = getConfig().getString("network.username");
        String password = getConfig().getString("network.password");

        network = new Network(getLogger(), url, username, password);
        if (network.init()) {
            getLogger().info("Database find!");
        } else {
            getLogger().severe("Database not find!");
            plugin.setEnabled(false);
        }
        network.createDB();


    }

    Network getNetwork() {
        return network;
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
        //network.close();

    }
}
