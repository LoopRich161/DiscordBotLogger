package ru.frostdelta.discord;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.looprich.discordlogger.Authentication;
import ru.looprich.discordlogger.Deauthentication;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.module.DiscordBot;

public class BotCommand implements CommandExecutor {

    public static void reg() {
        DiscordLogger.getInstance().getCommand("bot").setExecutor(new BotCommand());
        DiscordLogger.getInstance().getCommand("deauthentication").setExecutor(new BotCommand());
        DiscordLogger.getInstance().getCommand("authentication").setExecutor(new BotCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String who = sender.getName();
        if (command.getName().equalsIgnoreCase("authentication") && args.length != 0) {
            if (args[0].equalsIgnoreCase("accept")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Вы не ввели код подверждения!");
                    return true;
                }
                String code = args[1];
                if (DiscordLogger.getInstance().authentication.containsKey(sender.getName())) {
                    Authentication authentication = DiscordLogger.getInstance().authentication.get(sender.getName());
                    if (authentication.getCode().equalsIgnoreCase(code)) {
                        authentication.accept();
                    } else sender.sendMessage(ChatColor.RED + "Код подверждения неверный!");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("reject")) {
                for (Authentication var1 : DiscordLogger.getInstance().authentication.values()) {
                    if (var1.getPlayerName().equalsIgnoreCase(who)) {
                        var1.reject();
                        return true;
                    }
                }
            } else {
                sender.sendMessage(ChatColor.GOLD + "Доступные команды:  /authentication accept <code> - для завершения аутентификации.\n" +
                        "/authentication reject - для отказа в завершении аутентификации");
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("deauthentication")) {
            Deauthentication deauthentication = new Deauthentication((Player) sender);
            deauthentication.deauth();
            return true;
        }
        if (command.getName().equalsIgnoreCase("bot") && args.length == 1) {
            switch (args[0]) {
                case "help":
                    sender.sendMessage(ChatColor.GOLD + "Доступные команды:\n" +
                            ChatColor.DARK_PURPLE + "/toggle - переключение локального чата.\n" +
                            ChatColor.DARK_PURPLE + "/bot reload - перезагрузка конфига.\n" +
                            ChatColor.DARK_PURPLE + "/bot disable - выключение бота.\n" +
                            ChatColor.DARK_PURPLE + "/bot restart - перезагрузка бота.\n" +
                            ChatColor.DARK_PURPLE + "/bot developers - информация о разработчиках.\n" +
                            ChatColor.DARK_PURPLE + "/bot deauthentication - отвязать свой аккаунт от Discord.");
                    break;
                case "toggle":
                    if (DiscordBot.isLocalEnabled()) {
                        DiscordBot.setLocalEnabled(false);
                        DiscordBot.sendImportantMessage("Локальный чат отключен! (" + who + ")");
                        sender.sendMessage(ChatColor.GREEN + "Локальный чат отключен!");
                        DiscordLogger.getInstance().getConfig().set("local-chat", false);
                    } else {
                        DiscordBot.setLocalEnabled(true);
                        DiscordBot.sendImportantMessage("Локальный чат включен! (" + who + ")");
                        sender.sendMessage(ChatColor.GREEN + "Локальный чат включен!");
                        DiscordLogger.getInstance().getConfig().set("local-chat", true);
                    }
                    break;
                case "reload":
                    DiscordLogger.getInstance().reloadConfig();
                    sender.sendMessage(ChatColor.GREEN + "Бот перезагрузил конфиг!");
                    break;
                case "enable":
                    if (!DiscordBot.isEnabled()) {
                        DiscordLogger.getInstance().loadDiscordBot();
                        DiscordBot.sendImportantMessage("Я включился! (" + who + ")");
                        sender.sendMessage(ChatColor.GREEN + "Бот успешно включен");
                    } else sender.sendMessage(ChatColor.RED + "Бот уже включен!");
                    break;
                case "disable":
                    if (!DiscordBot.isEnabled()) {
                        DiscordLogger.getInstance().getLogger().info("Бот уже выключен!");
                        return true;
                    }
                    DiscordBot.sendImportantMessage("Я выключился! (" + who + ")");
                    DiscordBot.shutdown();
                    sender.sendMessage(ChatColor.GREEN + "Бот успешно выключен!");
                    break;
                case "restart":
                    if (DiscordBot.isEnabled())
                        DiscordBot.getJDA().shutdownNow();
                    DiscordLogger.getInstance().loadDiscordBot();
                    sender.sendMessage(ChatColor.GREEN+"Бот успешно перезагружен!");
                    DiscordBot.sendImportantMessage("Я перезагрузился! (" + who + ")");
                    break;
                case "developers":
                    sender.sendMessage(ChatColor.GOLD+"LoopRich161 - создатель плагина.\n" +
                            "                 FrostDelta123 - человек-идея, а так же фиксящий ошибки и исправляющий костыли.");
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Доступные команды: " + ChatColor.GOLD + "/bot <help/enable/disable/restart>");
                    break;
            }
        }
        return true;
    }

}
