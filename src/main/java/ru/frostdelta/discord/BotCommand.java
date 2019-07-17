package ru.frostdelta.discord;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.authentication.GameAuthentication;
import ru.looprich.discordlogger.deauthentication.GameDeauthentication;
import ru.looprich.discordlogger.modules.DiscordBot;

public class BotCommand implements CommandExecutor {

    public static void reg() {
        DiscordLogger.getInstance().getCommand("bot").setExecutor(new BotCommand());
        DiscordLogger.getInstance().getCommand("deauthentication").setExecutor(new BotCommand());
        DiscordLogger.getInstance().getCommand("authentication").setExecutor(new BotCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String who = sender.getName();

        //Какие-то костыли честно говоря, я думаю с мапами в разы проще сделать можно
        if (command.getName().equalsIgnoreCase("authentication") && args.length == 2) {
            String code = args[1];
            if (args[0].equalsIgnoreCase("code")) {
                for (GameAuthentication gameAuthentication : DiscordLogger.getInstance().gameAuthenticationUsers) {
                    if (gameAuthentication.getPlayer().getName().equalsIgnoreCase(who)) {
                        if (gameAuthentication.getCode().equalsIgnoreCase(code)) {
                            gameAuthentication.accept();
                            return true;
                        }
                        sender.sendMessage("Код подтверждения неверный!");
                        gameAuthentication.reject();
                        return true;
                    }
                }
            } else sender.sendMessage("Использование команды: /authentication code <code>");
        }

        /*if (command.getName().equalsIgnoreCase("deauthentication") && args.length == 1) {
            if (sender instanceof Player) {
                String userAsTag = args[0];
                for (GameDeauthentication deauthentication : DiscordLogger.getInstance().gameDeauthenticationPlayers) {
                    if (deauthentication.getPlayerName().equalsIgnoreCase(sender.getName())) {
                        sender.sendMessage("Данному участнику уже выслан код! Ожидайте пока закончится время для подтверждения кода.");
                        return true;
                    }
                }
                GameDeauthentication gameDeauthentication = new GameDeauthentication((Player) sender, userAsTag);
                gameDeauthentication.deauthentication();

            } else sender.sendMessage("Только для игроков!");
            return true;
        }*/

        if (command.getName().equalsIgnoreCase("deauthentication") && args.length == 2) {
            if (sender instanceof Player) {
                if (args[0].equalsIgnoreCase("code")) {
                    String code = args[1];
                    for (GameDeauthentication deauthentication : DiscordLogger.getInstance().gameDeauthenticationPlayers) {
                        if (deauthentication.getPlayerName().equalsIgnoreCase(sender.getName())) {
                            if (code.equalsIgnoreCase(deauthentication.getCode())) {
                                deauthentication.accept();
                                return true;
                            }
                            sender.sendMessage("Код подтверждения неверный!");
                            deauthentication.reject();
                            return true;
                        }
                    }
                } else sender.sendMessage("Использование команды: /deauthentication code <code>");

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
                        DiscordBot.sendImportantMessage("Локальный чат отключен! (" + who + ")");
                        sender.sendMessage("Локальный чат отключен!");
                        DiscordLogger.getInstance().getConfig().set("local-chat", false);
                    } else {
                        DiscordBot.setLocalEnabled(true);
                        DiscordBot.sendImportantMessage("Локальный чат включен! (" + who + ")");
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
                    DiscordBot.sendImportantMessage("Я выключился! (" + who + ")");
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
                    DiscordBot.sendImportantMessage("Я перезагрузился! (" + who + ")");
                    break;
                case "developers":
                    sender.sendMessage("LoopRich161 - создатель плагина.\n" +
                            "                 FrostDelta123 - человек-идея, а так же фиксящий ошибки и исправляющий костыли.");
                default:
                    sender.sendMessage(ChatColor.RED + "Доступные команды: " + ChatColor.GOLD + "/bot <help/enable/disable/restart>");
                    break;
            }
        }
        return true;
    }

}
