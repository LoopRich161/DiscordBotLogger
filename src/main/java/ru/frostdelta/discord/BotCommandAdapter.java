package ru.frostdelta.discord;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.modules.DiscordBot;

import java.util.List;

public class BotCommandAdapter extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //проверка на привязку
        if (DiscordBot.commandOnlyOneChannel)
            if (!event.getTextChannel().getId().equalsIgnoreCase(DiscordBot.getLoggerChannel().getId()) && event.getTextChannel() != null)
                return;

        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args.length == 0) {
            return;
        }
        String command = args[0];
        if(command.equalsIgnoreCase(DiscordBot.prefix + "command")){
            if (DiscordBot.isIsWhitelistEnabled()){
                List<String> whitelist = DiscordLogger.getInstance().getConfig().getStringList("whitelist");
                for(String allowedCmd : whitelist){
                    if (args[1].contains(allowedCmd)){
                        //чек перма и вся хуйня
                        break;
                    }
                }
                //блеклист
                //TODO добавить парсер команд в зависимости от white/black листа
            }
        }
    }

}
