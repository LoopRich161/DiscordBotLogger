package ru.frostdelta.discord;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.modules.DiscordBot;
import ru.looprich.discordlogger.snapping.GameAuthentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.looprich.discordlogger.modules.DiscordBot.sendImportantMessage;

public class RemoteConfigControl extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (DiscordBot.commandOnlyOneChannel)
            if (!event.getTextChannel().getId().equalsIgnoreCase(DiscordBot.getLoggerChannel().getId()) && event.getTextChannel() != null)
                return;

        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args.length == 0) {
            return;
        }
        String command = args[0];
        if (command.equalsIgnoreCase(DiscordBot.prefix + "bot") && args[1].equalsIgnoreCase("help")) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Помощь");
            info.setDescription("Доступные команды:\n" +
                    "~bot toggle - *переключение локального чата.*\n" +
                    "~bot reload - *перезагрузка конфига.*\n" +
                    "~bot disable - *выключение бота.*\n" +
                    "~bot restart - *перезагрузка бота.*\n" +
                    "~bot developers - *информация о разработчиках.*\n" +
                    "~bot online - *просмотреть кто находится на сервере.\n" +
                    "~verify <nickname> - *привязать Discord к Minecraft аккаунту. Необходимо открыть личные сообщения от участников сервера!*");
            info.addField("Создатели", Arrays.toString(DiscordLogger.getInstance().getDescription().getAuthors().toArray()), false);
            info.setColor(0x008000);

            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(info.build()).queue();
            return;
        }

        String who = event.getAuthor().getAsTag();

        if (command.equalsIgnoreCase(DiscordBot.prefix + "toggle")) {
            if (DiscordBot.isLocalEnabled()) {
                DiscordBot.setLocalEnabled(false);
                sendImportantMessage("Локальный чат отключен! (" + who + ")");
                DiscordLogger.getInstance().getConfig().set("local-chat", false);
            } else {
                DiscordBot.setLocalEnabled(true);
                sendImportantMessage("Локальный чат включен! (" + who + ")");
                DiscordLogger.getInstance().getConfig().set("local-chat", true);
            }
            DiscordLogger.getInstance().getLogger().info("<" + who + "> issued discord command: ~toggle");
            return;
        }

        if (command.equalsIgnoreCase(DiscordBot.prefix + "verify") && args.length == 2) {
            String nickname = args[1];
            for (GameAuthentication gameSnapping : DiscordLogger.getInstance().verifyUsers)
                if (gameSnapping.getPlayerName().equalsIgnoreCase(nickname)) {
                    DiscordBot.sendVerifyMessage("Данному участнику уже выслан код! Ожидайте пока закончится время для подтверждения кода.");
                    return;
                }
            GameAuthentication snapping = new GameAuthentication(event.getAuthor(), nickname);
            snapping.regPlayer();
            DiscordLogger.getInstance().getLogger().info("<" + who + "> issued discord command: ~verify " + nickname);
            return;
        }

        if (command.equalsIgnoreCase(DiscordBot.prefix + "bot") && args.length == 2) {
            switch (args[1]) {
                case "reload":
                    DiscordLogger.getInstance().reloadConfig();
                    sendImportantMessage("Я перезагрузил конфиг! (" + who + ")");
                    break;
                case "disable":
                    sendImportantMessage("Я выключился!");
                    DiscordLogger.getInstance().getLogger().info("<" + who + "> issued discord command: ~bot disable");
                    DiscordBot.shutdown();
                    break;
                case "restart":
                    if (DiscordBot.isEnabled()) {
                        sendImportantMessage("Я выключился!");
                        DiscordBot.getJDA().shutdownNow();
                        DiscordLogger.getInstance().loadDiscordBot();
                    } else {
                        DiscordLogger.getInstance().loadDiscordBot();
                    }
                    DiscordLogger.getInstance().getLogger().info("<" + who + "> issued discord command: ~bot restart");
                    sendImportantMessage("Я перезагрузился! (" + who + ")");
                    break;
                case "developers":
                    EmbedBuilder developers = new EmbedBuilder();
                    developers.setTitle("Разработчики");
                    developers.setDescription("LoopRich161 - *создатель плагина.*\n" +
                            "FrostDelta123 - *человек-идея, а так же фиксящий ошибки и исправляющий костыли.*");
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
                            + "\nИгроки: " + onlinePlayers.toString().replace("[", "").replace("]", ""));
                    online.setColor(0xFFFF00);

                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage(online.build()).queue();
                    break;
                default:
                    event.getChannel().sendMessage("Доступные команды: ~bot <disable/restart/developers/online>");
                    break;
            }
        }

    }

}
