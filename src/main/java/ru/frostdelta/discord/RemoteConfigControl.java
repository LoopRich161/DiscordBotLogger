package ru.frostdelta.discord;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.modules.DiscordBot;

public class RemoteConfigControl extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args.length == 0 || !(args[0].equalsIgnoreCase(DiscordBot.prefix))) {
            return;
        }
        String command = args[1];
        if (args.length == 2 && command.equalsIgnoreCase("help")) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Помощь");
            info.setDescription("Просмотрев данную информацию станет легче что-то там.");
            info.addField("Создатели", DiscordLogger.getInstance().getDescription().getAuthors().toString(), false);
            info.setColor(0xf45642);

            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(info.build()).queue();
            return;
        }
        if (command.equalsIgnoreCase("toggle")&& args.length == 2) {
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

        if (command.equalsIgnoreCase("bot") && args.length == 3) {
            switch (args[2]) {
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
