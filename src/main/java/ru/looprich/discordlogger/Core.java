package ru.looprich.discordlogger;

import org.bukkit.plugin.java.JavaPlugin;
import ru.frostdelta.discord.BotCommand;
import ru.looprich.discordlogger.events.EventHandlers;
import ru.looprich.discordlogger.modules.DiscordBot;

public class Core extends JavaPlugin {
    private static Core plugin;
    private boolean isEnabled;
    public DiscordBot discordBot;

    @Override
    public void onEnable() {
        this.getLogger().info("Developed by " + getDescription().getAuthors());
        plugin = this;
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new EventHandlers(), this);
        isEnabled = getConfig().getBoolean("bot.enabled");
        if(isEnabled) {
            getLogger().info("DiscordBotLogging enabled!");
            getLogger().info("Loading...");
            loadDiscordBot();
            BotCommand.reg();
        }else getLogger().info("DiscordBotLogging disabled!");
    }

    public void loadDiscordBot() {
        String token = getConfig().getString("bot.token");
        String channel = getConfig().getString("bot.channel-id");
        discordBot = new DiscordBot(token, channel);
        getLogger().info("Bot successful loaded!");
        if (!discordBot.createBot()) {
            System.out.println("PLUGIN DISABLE! YOU HAVE PROBLEMS WITH DISCORD BOT!");
            getPluginLoader().disablePlugin(this);
        }
    }

    public static Core getInstance() {
        return plugin;
    }

    public void sendMessageDiscord(String message) {
        if (!DiscordBot.isEnabled())return;
        DiscordBot.sendMessageChannel(message);
    }

    @Override
    public void onDisable() {
        if (DiscordBot.isEnabled()){
            DiscordBot.shutdown();
        }
    }
}
