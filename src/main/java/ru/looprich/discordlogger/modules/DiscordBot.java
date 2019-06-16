package ru.looprich.discordlogger.modules;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.TextChannel;
import ru.frostdelta.discord.RemoteConfigControl;
import ru.looprich.discordlogger.DiscordLogger;

import javax.security.auth.login.LoginException;
import java.util.Date;

public class DiscordBot {

    private static DiscordBot bot;
    private static boolean localEnabled;
    private static String tokenBot;
    private static String channel;
    private static TextChannel loggerChannel = null;
    private static JDA jda = null;
    private static boolean enable;
    public static final String prefix = "~";

    public DiscordBot(String tokenBot, String channel) {
        DiscordBot.tokenBot = tokenBot;
        DiscordBot.channel = channel;
    }

    public static void sendMessage(String message) {
        message.replace("*", "$").replace("_", "$").replace("~", "$");
        DiscordLogger.getInstance().sendMessageDiscord(message);
    }

    public boolean createBot() {
        try {
            jda = new JDABuilder(tokenBot).build().awaitReady();
            jda.getPresence().setStatus(OnlineStatus.IDLE);
            jda.getPresence().setGame(Game.watching("за вашим сервером."));
            jda.addEventListener(new RemoteConfigControl());
        } catch (LoginException e) {
            System.out.println("Invalid token!");
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loggerChannel = jda.getTextChannelById(channel);
        bot = this;
        localEnabled = DiscordLogger.getInstance().getConfig().getBoolean("bot.local-chat");
        enable = true;
        DiscordBot.sendMessage("Bot successful loaded!");
        return loggerChannel != null;
    }

    public static DiscordBot getBot() {
        return bot;
    }

    public static boolean isEnabled() {
        return enable;
    }

    public static void shutdown() {
        DiscordBot.sendMessage("Bot shutdown!");
        bot = null;
        localEnabled = true;
        tokenBot = null;
        channel = null;
        loggerChannel = null;
        enable = false;
        DiscordLogger.getInstance().discordBot = null;
        jda.shutdownNow();
    }

    public static void setLocalEnabled(boolean localEnabled) {
        DiscordBot.localEnabled = localEnabled;
    }

    public static boolean isLocalEnabled() {
        return localEnabled;
    }

    public static void sendMessageChannel(String message) {
        loggerChannel.sendMessage(data() + message).queue();
    }

    private static String data() {
        Date date = new Date();
        String hours, minutes, seconds;
        if (date.getHours() < 10) hours = "0" + date.getHours();
        else hours = String.valueOf(date.getHours());
        if (date.getMinutes() < 10) minutes = "0" + date.getMinutes();
        else minutes = String.valueOf(date.getMinutes());
        if (date.getSeconds() < 10) seconds = "0" + date.getSeconds();
        else seconds = String.valueOf(date.getSeconds());
        return "**[" + hours + ":" + minutes + ":" + seconds + "]:** ";
    }
}
