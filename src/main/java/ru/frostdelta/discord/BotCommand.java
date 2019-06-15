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
        BotCommand botCommand = new BotCommand();
        Core.getInstance().getCommand("bot").setExecutor(botCommand);
        Core.getInstance().getCommand("toggle").setExecutor(botCommand);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "Only for console!");
            return true;
        }
        if (command.getName().equalsIgnoreCase("toggle")) {
            if (DiscordBot.isLocalEnabled()) {
                DiscordBot.setLocalEnabled(false);
                EventHandlers.sendMessage("Локальный чат отключен!");
                Core.getInstance().getLogger().info("Local chat disable!");
            } else {
                DiscordBot.setLocalEnabled(true);
                EventHandlers.sendMessage("Локальный чат включен!");
                Core.getInstance().getLogger().info("Local chat enable!");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("bot") && args.length == 1) {
            switch (args[0]) {
                case "enable":
                    if (!DiscordBot.isEnabled()) {
                        Core.getInstance().loadDiscordBot();
                        Core.getInstance().getLogger().info("Bot successful enabled!");
                    } else Core.getInstance().getLogger().info("Bot already enabled!");
                    break;
                case "disable":
                    DiscordBot.sendMessageChannel("Bot successful disabled!");
                    DiscordBot.shutdown();
                    Core.getInstance().getLogger().info("Bot successful disabled!");
                    break;
                case "restart":
                    if (DiscordBot.isEnabled()) {
                        DiscordBot.shutdown();
                        Core.getInstance().loadDiscordBot();
                        Core.getInstance().getLogger().info("Bot successful restarted!");
                    } else {
                        Core.getInstance().loadDiscordBot();
                        Core.getInstance().getLogger().info("Bot successful restarted!");
                    }
                    break;
            }
        } else sender.sendMessage(ChatColor.RED + "Command usage: " + ChatColor.GOLD + "/bot <enable/disable/restart>");
        return true;
    }

}
