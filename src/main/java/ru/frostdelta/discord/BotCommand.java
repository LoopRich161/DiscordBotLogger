package ru.frostdelta.discord;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.modules.DiscordBot;
import ru.looprich.discordlogger.snapping.GameSnapping;

import static ru.looprich.discordlogger.modules.DiscordBot.sendImportantMessage;

public class BotCommand implements CommandExecutor {

    public static void reg() {
        DiscordLogger.getInstance().getCommand("bot").setExecutor(new BotCommand());
        DiscordLogger.getInstance().getCommand("verify").setExecutor(new BotCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String who = sender.getName();

        if (command.getName().equalsIgnoreCase("verify") && args.length == 2) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                for (GameSnapping snapping : DiscordLogger.getInstance().verifyUsers) {
                    if (snapping.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                        switch (args[1]) {
                            case "accept":
                                snapping.accept();
                                break;
                            case "reject":
                                snapping.reject();
                                break;
                            default:
                                player.sendMessage(ChatColor.RED + "Доступные ответы: " + ChatColor.GOLD + "/verify <accept <code>/reject>");
                                break;
                        }
                        return true;
                    }
                }
            } else sender.sendMessage("Только для игроков!");
            return true;
        }

        if (command.getName().equalsIgnoreCase("bot") && args.length == 1) {
            switch (args[0]) {
                case "help":
                    sender.sendMessage("Доступные команды:\n" +
                            "~toggle - *переключение локального чата.*\n" +
                            "~bot reload - *перезагрузка конфига.*\n" +
                            "~bot disable - *выключение бота.*\n" +
                            "~bot restart - *перезагрузка бота.*\n" +
                            "~bot developers - *информация о разработчиках.*");
                    break;
                case "toggle":
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
                    break;
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
                    if (!DiscordBot.isEnabled()) {
                        DiscordLogger.getInstance().getLogger().info("Бот уже выключен!");
                        return true;
                    }
                    sendImportantMessage("Я выключился! (" + who + ")");
                    DiscordBot.shutdown();
                    sender.sendMessage("Бот успешно выключен!");
                    break;
                case "restart":
                    if (DiscordBot.isEnabled()) {
                        DiscordBot.getJDA().shutdownNow();
                        DiscordLogger.getInstance().loadDiscordBot();
                        sender.sendMessage("Бот успешно перезагружен!");
                    } else {
                        DiscordLogger.getInstance().loadDiscordBot();
                        sender.sendMessage("Бот успешно перезагружен!");
                    }
                    sendImportantMessage("Я перезагрузился! (" + who + ")");
                    break;
                case "developers":
                    sender.sendMessage("LoopRich161 - создатель плагина.\n" +
                            "                 FrostDelta123 - человек-идея, а так же фиксящий ошибки и исправляющий костыли.");
            }
        } else
            sender.sendMessage(ChatColor.RED + "Доступные команды: " + ChatColor.GOLD + "/bot <help/enable/disable/restart>");
        return true;
    }

}
