package ru.looprich.discordlogger;

import org.bukkit.plugin.java.JavaPlugin;
import ru.looprich.discordlogger.events.EventHandlers;
import ru.looprich.discordlogger.modules.DiscordBot;

public class Core extends JavaPlugin {
    private static Core plugin;
    private DiscordBot discordBot;

    @Override
    public void onEnable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(new EventHandlers(), this);
        loadDiscordBot();
    }

    private void loadDiscordBot() {
        String token = getConfig().getString("bot.token");
        String channel = getConfig().getString("bot.channel-id");
        discordBot = new DiscordBot(token, channel);
        if (!discordBot.createBot()) {
            System.out.println("PLUGIN DISABLE! YOU HAVE PROBLEMS WITH DISCORD BOT!");
            getPluginLoader().disablePlugin(this);
        }
    }

    public static Core getInstance() {
        return plugin;
    }

    public void sendMessageDiscord(String message) {
        discordBot.sendMessageChannel(message);
    }

    @Override
    public void onDisable() {
        discordBot.getJDA().shutdownNow();
    }
}
