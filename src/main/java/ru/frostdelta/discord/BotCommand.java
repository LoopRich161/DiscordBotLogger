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

        String who = sender.getName();
        if (command.getName().equalsIgnoreCase("toggle")) {
            if (DiscordBot.isLocalEnabled()) {
                DiscordBot.setLocalEnabled(false);
                sendImportantMessage("Локальный чат отключен! (" + who + ")");
                sender.sendMessage("Локальный чат отключен!");
                DiscordLogger.getInstance().getConfig().set("local-chat", false);
            } else {
                DiscordBot.setLocalEnabled(true);
                sendImportantMessage("Локальный чат включен! (" + who + ")");
                sender.sendMessage("Локальный чат включен!");
                DiscordLogger.getInstance().getConfig().set("local-chat", true);
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("bot") && args.length == 1) {
            switch (args[0]) {
                case "reload":
                    DiscordLogger.getInstance().reloadConfig();
                    sender.sendMessage("Бот перезагрузил конфиг!");
                    break;
                case "enable":
                    if (!DiscordBot.isEnabled()) {
                        DiscordLogger.getInstance().loadDiscordBot();
                        DiscordBot.sendImportantMessage("Я включился! (" + who + ")");
                        sender.sendMessage("Бот успешно включен");
                    } else sender.sendMessage("Бот уже включен!");
                    break;
                case "disable":
                    if (!DiscordBot.isEnabled()){
                        DiscordLogger.getInstance().getLogger().info("Бот уже выключен!");
                        return true;
                    }
                    sendImportantMessage("Я выключился! (" + who + ")");
                    DiscordBot.shutdown();
                    sender.sendMessage("Бот успешно выключен!");
                    break;
                case "restart":
                    if (DiscordBot.isEnabled()) {
                        sendImportantMessage("Я выключился!");
                        DiscordBot.getJDA().shutdownNow();
                        DiscordLogger.getInstance().loadDiscordBot();
                        sender.sendMessage("Бот успешно перезагружен!");
                    } else {
                        DiscordLogger.getInstance().loadDiscordBot();
                        sender.sendMessage("Бот успешно перезагружен!");
                    }
                    break;
            }
        } else sender.sendMessage(ChatColor.RED + "Доступные команды: " + ChatColor.GOLD + "/bot <enable/disable/restart>");
        return true;
    }

}
