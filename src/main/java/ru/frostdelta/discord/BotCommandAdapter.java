package ru.frostdelta.discord;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import ru.looprich.discordlogger.modules.DiscordBot;

public class BotCommandAdapter extends ListenerAdapter {

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
        if(command.equalsIgnoreCase(DiscordBot.prefix + "command")){

        }
    }

}
