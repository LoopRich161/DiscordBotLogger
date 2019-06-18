package ru.frostdelta.discord;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Color;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.modules.DiscordBot;

import java.util.Arrays;

public class RemoteConfigControl extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args.length == 0) {
            return;
        }
        String command = args[0];
        if (command.equalsIgnoreCase(DiscordBot.prefix+"help")) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Помощь");
            info.setDescription("Доступные команды:\n" +
                    "~toggle - *переключение локального чата.*\n" +
                    "~bot reload - *перезагрузка конфига.*\n" +
                    "~bot disable - *выключение бота.*\n" +
                    "~bot restart - *перезагрузка бота.*\n" +
                    "~developers - *информация о разработчиках.*");
            info.addField("Создатели", Arrays.toString(DiscordLogger.getInstance().getDescription().getAuthors().toArray()), false);
            info.setColor(0x008000);

            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(info.build()).queue();
            return;
        }
        if (command.equalsIgnoreCase(DiscordBot.prefix+"toggle")) {
            if (DiscordBot.isLocalEnabled()) {
                DiscordBot.setLocalEnabled(false);
                DiscordBot.sendImportantMessage("Локальный чат отключен!");
                DiscordLogger.getInstance().getConfig().set("local-chat", false);
            } else {
                DiscordBot.setLocalEnabled(true);
                DiscordBot.sendImportantMessage("Локальный чат включен!");
                DiscordLogger.getInstance().getConfig().set("local-chat", true);
            }
            return;
        }

        if (command.equalsIgnoreCase(DiscordBot.prefix+"developers")){
            EmbedBuilder developers = new EmbedBuilder();
            developers.setTitle("Разработчики");
            developers.setDescription("LoopRich161 - *.*\n" +
                    "FrostDelta123 - *.*");
            developers.setColor(0x0000ff);

            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(developers.build()).queue();
            return;
        }

        if (command.equalsIgnoreCase(DiscordBot.prefix+"bot") && args.length == 2) {
            switch (args[1]) {
                case "reload":
                    DiscordLogger.getInstance().reloadConfig();
                    event.getChannel().sendMessage("Bot already enabled!");
                    break;
                case "disable":
                    DiscordBot.shutdown();
                    event.getChannel().sendMessage("Bot successful disabled!");
                    break;
                case "restart":
                    if (DiscordBot.isEnabled()) {
                        DiscordBot.shutdown();
                        DiscordLogger.getInstance().loadDiscordBot();
                        event.getChannel().sendMessage("Bot successful restarted!");
                    } else {
                        DiscordLogger.getInstance().loadDiscordBot();
                        event.getChannel().sendMessage("Bot successful restarted!");
                    }
                    break;
            }
        } else event.getChannel().sendMessage("Command usage: /bot <disable/restart>");


    }

}
