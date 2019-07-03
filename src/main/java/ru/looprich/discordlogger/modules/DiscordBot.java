package ru.looprich.discordlogger.modules;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.TextChannel;
import ru.frostdelta.discord.BotCommandAdapter;
import ru.frostdelta.discord.RemoteConfigControl;
import ru.looprich.discordlogger.DiscordLogger;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;

public class DiscordBot {

    private static DiscordBot bot;
    private static boolean localEnabled;
    private static String tokenBot;
    private static String channel;
    private static TextChannel loggerChannel = null;
    private static JDA jda = null;
    private static boolean enable;
    public static final String prefix = "~";
    public static boolean commandOnlyOneChannel;

    public DiscordBot(String tokenBot, String channel) {
        DiscordBot.tokenBot = tokenBot;
        DiscordBot.channel = channel;
    }

    public static void shutdown() {
        bot = null;
        localEnabled = true;
        tokenBot = null;
        channel = null;
        loggerChannel = null;
        enable = false;
        DiscordLogger.getInstance().discordBot = null;
        jda.shutdown();
    }

    public static void sendMessageChannel(String message) {
        if (!isEnabled()) return;
        String msg = cancelFormatMessage(message);
        loggerChannel.sendMessage(getDate() + msg).queue();
    }

    public static void sendImportantMessage(String msg) {
        EmbedBuilder message = new EmbedBuilder();
        message.setTitle("Что-то произошло с ботом!");
        message.setDescription(msg);
        message.setColor(0xf45642);
        loggerChannel.sendTyping().queue();
        loggerChannel.sendMessage(message.build()).queue();
    }

    private static String cancelFormatMessage(String message) {
        String[] array = message.split(" ");
        StringBuilder msg = new StringBuilder();
        int pos1, pos2, difference;
        for (int i = 0; i <= array.length - 1; i++) {
            StringBuilder buff = new StringBuilder(" " + array[i]);

            pos1 = buff.toString().indexOf('_');
            pos2 = buff.toString().lastIndexOf('_');
            difference = Math.max(pos1, pos2) - Math.min(pos1, pos2);
            if (pos1 != pos2 && difference != 1) buff.insert(pos1, "\\");

            pos1 = buff.toString().indexOf('*');
            pos2 = buff.toString().lastIndexOf('*');
            difference = Math.max(pos1, pos2) - Math.min(pos1, pos2);
            if (pos1 != pos2 && difference != 1) buff.insert(pos1, "\\");

            pos1 = buff.toString().indexOf('~');
            pos2 = buff.toString().lastIndexOf('~');
            difference = Math.max(pos1, pos2) - Math.min(pos1, pos2);
            if (pos1 != pos2 && difference != 1) buff.insert(pos1, " \\");

            msg.append(buff.toString());
        }
        return msg.toString();
    }

    private static String getDate() {
        LocalDateTime time = LocalDateTime.now();
        String hours, minutes, seconds;
        if (time.getHour() < 10) hours = "0" + time.getHour();
        else hours = String.valueOf(time.getHour());
        if (time.getMinute() < 10) minutes = "0" + time.getMinute();
        else minutes = String.valueOf(time.getMinute());
        if (time.getSecond() < 10) seconds = "0" + time.getSecond();
        else seconds = String.valueOf(time.getSecond());
        return "**[" + hours + ":" + minutes + ":" + seconds + "]:** ";
    }

    public static void sendVerifyMessage(String msg) {
        EmbedBuilder message = new EmbedBuilder();
        message.setTitle("Верификация аккаунтов.");
        message.setDescription(msg);
        message.setColor(0x800080);
        loggerChannel.sendTyping().queue();
        loggerChannel.sendMessage(message.build()).queue();
    }

    public static TextChannel getLoggerChannel() {
        return loggerChannel;
    }

    public static JDA getJDA() {
        return jda;
    }

    public boolean createBot() {
        try {
            jda = new JDABuilder(tokenBot).build().awaitReady();
            jda.getPresence().setStatus(OnlineStatus.IDLE);
            jda.getPresence().setGame(Game.watching("за вашим сервером."));
            jda.addEventListener(new RemoteConfigControl());
            jda.addEventListener(new BotCommandAdapter());
        } catch (LoginException e) {
            DiscordLogger.getInstance().getLogger().severe("Invalid token!");
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loggerChannel = jda.getTextChannelById(channel);
        if (loggerChannel == null) {
            DiscordLogger.getInstance().getLogger().severe("Invalid channel!");
            return false;
        }
        bot = this;
        localEnabled = DiscordLogger.getInstance().getConfig().getBoolean("bot.local-chat");
        commandOnlyOneChannel = DiscordLogger.getInstance().getConfig().getBoolean("bot.command-only-channel");
        enable = true;
        sendImportantMessage("Я включился!");
        return true;
    }

    public static boolean isEnabled() {
        return jda.getStatus().isInit();
    }

    public static boolean isLocalEnabled() {
        return localEnabled;
    }

    public static void setLocalEnabled(boolean localEnabled) {
        DiscordBot.localEnabled = localEnabled;
    }


}
