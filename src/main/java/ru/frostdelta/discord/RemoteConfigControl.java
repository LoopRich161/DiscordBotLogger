package ru.frostdelta.discord;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.authentication.GameAuthentication;
import ru.looprich.discordlogger.modules.DiscordBot;

import java.util.ArrayList;
import java.util.List;

public class RemoteConfigControl extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getTextChannel().getId().equalsIgnoreCase(DiscordBot.channel)) return;

        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args.length == 0) {
            return;
        }
        String command = args[0];
        if (command.equalsIgnoreCase(DiscordBot.prefix + "bot") && args.length == 2 && args[1].equalsIgnoreCase("help")) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Помощь");
            info.setDescription("Доступные команды:\n" +
                    "~bot toggle - *переключение локального чата.*\n" +
                    "~bot reload - *перезагрузка конфига.*\n" +
                    "~bot disable - *выключение бота.*\n" +
                    "~bot restart - *перезагрузка бота.*\n" +
                    "~authentication <nickname> - *привязать Discord к Minecraft аккаунту. Необходимо открыть личные сообщения от участников сервера!*\n" +
                    "~command <command> - *выполнить команду на Minecraft сервере*\n" +
                    "~chat <message> - *написать сообщение в чат Minecraft*\n" +
                    "~bot developers - *информация о разработчиках.*\n" +
                    "~bot online - *просмотреть кто находится на сервере.*\n" +
                    "~bot version - *текущая версия плагина*.\n");
            info.addField("Создатели", String.valueOf(DiscordLogger.getInstance().getDescription().getAuthors()), false);
            info.setColor(0x008000);
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(info.build()).queue();
            return;
        }

        String who = event.getAuthor().getAsTag();

        if (command.equalsIgnoreCase(DiscordBot.prefix + "authentication") && args.length == 2) {
            String nickname = args[1];
            for (GameAuthentication gameAuthentication : DiscordLogger.getInstance().gameAuthenticationUsers)
                if (gameAuthentication.getPlayerName().equalsIgnoreCase(nickname)) {
                    DiscordBot.sendVerifyMessage("Данному участнику уже выслан код! Ожидайте пока закончится время для подтверждения кода.");
                    return;
                }
            GameAuthentication gameAuthentication = new GameAuthentication(event.getAuthor(), nickname);
            gameAuthentication.authentication();
            DiscordLogger.getInstance().getLogger().info("<" + who + "> issued discord command: ~authentication " + nickname);
            return;
        }

        if (command.equalsIgnoreCase(DiscordBot.prefix + "authentication") && args.length == 3) {
            String code = args[2];
            if ("code".equalsIgnoreCase(args[1])) {
                for (GameAuthentication gameAuthentication : DiscordLogger.getInstance().gameAuthenticationUsers) {
                    if (gameAuthentication.getUser().getAsTag().equalsIgnoreCase(event.getAuthor().getAsTag())) {
                        if (gameAuthentication.getCode().equalsIgnoreCase(code)) {
                            gameAuthentication.accept();
                            return;
                        }
                        event.getChannel().sendMessage("Код подтверждения неверный!").queue();
                        gameAuthentication.reject();
                        return;
                    }
                }
            } else event.getChannel().sendMessage("Использование команды: /authentication code <code>").queue();

        }

        if (command.equalsIgnoreCase(DiscordBot.prefix + "bot") && args.length == 2) {
            if (!DiscordLogger.getInstance().getNetwork().existUser(event.getAuthor())) {
                DiscordBot.sendVerifyMessage("Вы не прошли верификацию!");
                return;
            }
            switch (args[1]) {
                case "reload":
                    DiscordLogger.getInstance().reloadConfig();
                    DiscordBot.sendImportantMessage("Я перезагрузил конфиг! (" + who + ")");
                    break;
                case "disable":
                    DiscordBot.sendImportantMessage("Я выключился!");
                    DiscordLogger.getInstance().getLogger().info("<" + who + "> issued discord command: ~bot disable");
                    DiscordBot.shutdown();
                    break;
                case "restart":
                    if (DiscordBot.isEnabled()) {
                        DiscordBot.sendImportantMessage("Я выключился!");
                        DiscordBot.getJDA().shutdownNow();
                        DiscordLogger.getInstance().loadDiscordBot();
                    } else DiscordLogger.getInstance().loadDiscordBot();

                    DiscordLogger.getInstance().getLogger().info("<" + who + "> issued discord command: ~bot restart");
                    DiscordBot.sendImportantMessage("Я перезагрузился! (" + who + ")");
                    break;
                case "developers":
                    EmbedBuilder developers = new EmbedBuilder();
                    developers.setTitle("Разработчики");
                    developers.setDescription("LoopRich161 - *создатель плагина.*\n" +
                            "FrostDelta123 - *человек-идея, а так же фиксящий ошибки и исправляющий костыли, maintrainer.*");
                    developers.setColor(0x0000ff);
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage(developers.build()).queue();
                    break;
                case "online":
                    EmbedBuilder online = new EmbedBuilder();
                    List<String> onlinePlayers = new ArrayList<>();
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) onlinePlayers.add(player.getName());
                    online.setTitle("Онлайн сервера");
                    online.setDescription(Bukkit.getServer().getOnlinePlayers().size() + "/" + Bukkit.getServer().getMaxPlayers()
                            + "\nИгроки: " + Util.cancelFormatMessage(onlinePlayers.toString().replace("[", "").replace("]", "")));
                    online.setColor(0xFFFF00);
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage(online.build()).queue();
                    break;
                case "toggle":
                    if (DiscordBot.isLocalEnabled()) {
                        DiscordBot.setLocalEnabled(false);
                        DiscordBot.sendImportantMessage("Локальный чат отключен! (" + who + ")");
                        DiscordLogger.getInstance().getConfig().set("local-chat", false);
                    } else {
                        DiscordBot.setLocalEnabled(true);
                        DiscordBot.sendImportantMessage("Локальный чат включен! (" + who + ")");
                        DiscordLogger.getInstance().getConfig().set("local-chat", true);
                    }
                    DiscordLogger.getInstance().getLogger().info("<" + who + "> issued discord command: ~toggle");
                    break;
                case "version":
                    event.getChannel().sendMessage("Версия бота: v" + DiscordLogger.getInstance().getDescription().getVersion()).queue();
                    break;
                default:
                    event.getChannel().sendMessage("Доступные команды: ~bot <disable/restart/developers/online/toggle>").queue();
                    break;
            }
        }

    }

}
