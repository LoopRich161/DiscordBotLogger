package ru.frostdelta.discord;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.modules.DiscordBot;

import static ru.looprich.discordlogger.modules.DiscordBot.sendImportantMessage;

public class BotCommand implements CommandExecutor {

    public static void reg() {
        DiscordLogger.getInstance().getCommand("bot").setExecutor(new BotCommand());
        DiscordLogger.getInstance().getCommand("toggle").setExecutor(new BotCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("toggle")) {
            if (DiscordBot.isLocalEnabled()) {
                DiscordBot.setLocalEnabled(false);
                sendImportantMessage("Локальный чат отключен!");
                sender.sendMessage("Локальный чат отключен!");
                DiscordLogger.getInstance().getConfig().set("local-chat", false);
            } else {
                DiscordBot.setLocalEnabled(true);
                sendImportantMessage("Локальный чат включен!");
                sender.sendMessage("Локальный чат включен!");
                DiscordLogger.getInstance().getConfig().set("local-chat", true);
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("bot") && args.length == 1) {
            switch (args[0]) {
                case "reload":
                    DiscordLogger.getInstance().reloadConfig();
                    sender.sendMessage("Bot already enabled!");
                    break;
                case "enable":
                    if (!DiscordBot.isEnabled()) {
                        DiscordLogger.getInstance().loadDiscordBot();
                        sender.sendMessage("Bot successful enabled!");
                    } else  sender.sendMessage("Bot already enabled!");
                    break;
                case "disable":
                    sendImportantMessage("Я выключился!");
                    DiscordBot.shutdown();
                    sender.sendMessage("Bot successful disabled!");
                    break;
                case "restart":
                    if (DiscordBot.isEnabled()) {
                        sendImportantMessage("Я выключился!");
                        DiscordBot.shutdown();
                        DiscordLogger.getInstance().loadDiscordBot();
                        sender.sendMessage("Bot successful restarted!");
                    } else {
                        DiscordLogger.getInstance().loadDiscordBot();
                        sender.sendMessage("Bot successful restarted!");
                    }
                    break;
            }
        } else sender.sendMessage(ChatColor.RED + "Command usage: " + ChatColor.GOLD + "/bot <enable/disable/restart>");
        return true;
    }

}
