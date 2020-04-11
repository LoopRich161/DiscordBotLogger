package ru.frostdelta.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.looprich.discordlogger.Authentication;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.module.DiscordBot;

import java.util.ArrayList;
import java.util.List;

public class RemoteConfigControl extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (!event.getChannelType().equals(ChannelType.TEXT)||!event.getTextChannel().getId().equalsIgnoreCase(DiscordBot.channel)) return;
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args.length == 0) {
            return;
        }
        String command = args[0];
        if (command.equalsIgnoreCase(DiscordBot.prefix + "bot") && args.length == 2 && args[1].equalsIgnoreCase("help")) {
            EmbedBuilder info = new EmbedBuilder();
            String prefix = DiscordBot.getPrefix();
            info.setTitle("Помощь");
            info.setDescription("Доступные команды:\n" +
                    prefix + "bot toggle - *переключение локального чата.*\n" +
                    prefix + "bot reload - *перезагрузка конфига.*\n" +
                    prefix + "bot disable - *выключение бота.*\n" +
                    prefix + "bot restart - *перезагрузка бота.*\n" +
                    prefix + "auth <nickname> - *привязать Discord к Minecraft аккаунту. Необходимо открыть личные сообщения от участников сервера!*\n" +
                    prefix + "command <command> - *выполнить команду на Minecraft сервере от своего имени*\n" +
                    prefix + "dispatch <command> - *выполнить команду на Minecraft сервере от имени консоли*\n"+
                    prefix + "chat <message> - *написать сообщение в чат Minecraft*\n" +
                    prefix + "bot developers - *информация о разработчиках.*\n" +
                    prefix + "bot online - *просмотреть кто находится на сервере.*\n" +
                    prefix + "bot version - *текущая версия плагина*.\n");
            info.addField("Создатели", String.valueOf(DiscordLogger.getInstance().getDescription().getAuthors()), false);
            info.setColor(0x008000);
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(info.build()).queue();
            return;
        }

        String who = event.getAuthor().getAsTag();

        if (command.equalsIgnoreCase(DiscordBot.prefix + "auth") && args.length == 2) {
            String playerName = args[1];
            if (DiscordLogger.getInstance().authentication.containsKey(playerName)) {
                DiscordBot.sendVerifyMessage("Участник " + playerName + " в данный момент уже проходит аутентификацию!");
                return;
            }
            Authentication authentication = new Authentication(event.getAuthor(), playerName);
            authentication.auth();
            DiscordLogger.getInstance().getLogger().info("<" + who + "> issued discord command: "+DiscordBot.prefix + "auth");
            return;
        }

        if (command.equalsIgnoreCase(DiscordBot.prefix + "bot") && args.length == 2) {
            if (!DiscordLogger.getInstance().getNetwork().verifyUser(event.getAuthor())) {
                DiscordBot.sendVerifyMessage("Вы не прошли аутентификацию!");
                return;
            }
            switch (args[1]) {
                case "reload":
                    DiscordLogger.getInstance().reloadConfig();
                    DiscordBot.sendImportantMessage("Я перезагрузил конфиг! (" + who + ")");
                    DiscordLogger.getInstance().getLogger().info("<" + who + "> issued discord command: "+DiscordBot.prefix + "bot reload");
                    break;
                case "disable":
                    DiscordBot.sendImportantMessage("Я выключился!");
                    DiscordLogger.getInstance().getLogger().info("<" + who + "> issued discord command: "+DiscordBot.prefix + "bot disable");
                    DiscordBot.shutdown();
                    break;
                case "restart":
                    if (DiscordBot.isEnabled()) {
                        DiscordBot.sendImportantMessage("Я выключился!");
                        DiscordBot.getJDA().shutdownNow();
                    }
                    DiscordLogger.getInstance().loadDiscordBot();

                    DiscordLogger.getInstance().getLogger().info("<" + who + "> issued discord command: "+DiscordBot.prefix + "bot restart");
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
                    DiscordLogger.getInstance().getLogger().info("<" + who + "> issued discord command: "+DiscordBot.prefix + "bot toggle");
                    break;
                case "version":
                    event.getChannel().sendMessage("Версия бота: v" + DiscordLogger.getInstance().getDescription().getVersion()).queue();
                    break;
                default:
                    event.getChannel().sendMessage("Доступные команды: " + DiscordBot.getPrefix() + "bot <disable/restart/developers/online/toggle>").queue();
                    break;
            }
        }

    }

}
