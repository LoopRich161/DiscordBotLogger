package ru.frostdelta.discord;

import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import org.bukkit.ChatColor;
import ru.looprich.discordlogger.DiscordLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public static String buildCommand(String[] args) {
        StringBuilder cmd = new StringBuilder();
        for (String arg : args) {
            cmd.append(arg).append(" ");
        }
        return cmd.toString();
    }


    public static String getPermission() {
        return DiscordLogger.getInstance().getConfig().getString("admin-permission");
    }

    public static boolean checkContains(String command) {
        for (String cmd : getIgnoredCmds()) {
            if (command.contains(cmd)) return true;
        }
        return false;
    }

    public static List<String> getIgnoredCmds() {
        return DiscordLogger.getInstance().getConfig().getStringList("ignored-commands");
    }

    public static String removeCodeColors(String message) {
        return ChatColor.stripColor(message);
    }

    public static List<String> removeCodeColors(String[] messages) {
        List<String> message = new ArrayList<>();
        for (String msg : messages)
            message.add(ChatColor.stripColor(msg));
        return message;
    }

    public static String cancelFormatMessage(String message) {
        return MarkdownSanitizer.escape(message);
    }

    public static List<String> cancelFormatMessage(List<String> message) {
        List<String> result = new ArrayList<>();
        for (String msg : message) {
            result.add(MarkdownSanitizer.escape(msg));
        }
        return result;
    }

    public static boolean isDate(String date) {
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
        try {
            formatForDateNow.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
