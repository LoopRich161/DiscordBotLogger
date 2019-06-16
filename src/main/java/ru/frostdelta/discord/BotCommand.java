package ru.frostdelta.discord;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.looprich.discordlogger.Core;
import ru.looprich.discordlogger.events.EventHandlers;
import ru.looprich.discordlogger.modules.DiscordBot;

public class BotCommand implements CommandExecutor {

    public static void reg() {
        Core.getInstance().getCommand("bot").setExecutor(new BotCommand());
        Core.getInstance().getCommand("toggle").setExecutor(new BotCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("toggle")) {
            if (DiscordBot.isLocalEnabled()) {
                DiscordBot.setLocalEnabled(false);
                DiscordBot.sendMessage("Локальный чат отключен!");
                sender.sendMessage("Локальный чат отключен!");
                Core.getInstance().getConfig().set("local-chat", false);
            } else {
                DiscordBot.setLocalEnabled(true);
                DiscordBot.sendMessage("Локальный чат включен!");
                sender.sendMessage("Локальный чат включен!");
                Core.getInstance().getConfig().set("local-chat", true);
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("bot") && args.length == 1) {
            switch (args[0]) {
                case "reload":
                    Core.getInstance().reloadConfig();
                    sender.sendMessage("Bot already enabled!");
                    break;
                case "enable":
                    if (!DiscordBot.isEnabled()) {
                        Core.getInstance().loadDiscordBot();
                        DiscordBot.sendMessage("Bot successful disabled!");
                        sender.sendMessage("Bot successful enabled!");
                    } else  sender.sendMessage("Bot already enabled!");
                    break;
                case "disable":
                    DiscordBot.sendMessage("Bot successful disabled!");
                    DiscordBot.shutdown();
                    sender.sendMessage("Bot successful disabled!");
                    break;
                case "restart":
                    if (DiscordBot.isEnabled()) {
                        DiscordBot.shutdown();
                        Core.getInstance().loadDiscordBot();
                        sender.sendMessage("Bot successful restarted!");
                    } else {
                        Core.getInstance().loadDiscordBot();
                        sender.sendMessage("Bot successful restarted!");
                    }
                    break;
            }
        } else sender.sendMessage(ChatColor.RED + "Command usage: " + ChatColor.GOLD + "/bot <enable/disable/restart>");
        return true;
    }

}
