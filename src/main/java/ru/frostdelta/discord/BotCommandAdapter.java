package ru.frostdelta.discord;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.modules.DiscordBot;

import java.util.Arrays;
import java.util.List;

public class BotCommandAdapter extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //НЕБО УРОНИТ, НОЧЬ НА ЛАДОНИ
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args.length == 0) {
            return;
        }
        String botCommand = args[0];
        if(botCommand.equalsIgnoreCase(DiscordBot.prefix + "command")){
            if (!DiscordLogger.getInstance().getNetwork().existUser(event.getAuthor())) {
                DiscordBot.sendVerifyMessage("Вы не прошли верификацию!");
                return;
            }
            String cmd = buildCommand(Arrays.copyOfRange(args, 1, args.length));


            new SyncTasks(new FakePlayerCommandSender(event.getAuthor().getName()), cmd, Task.COMMAND);
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


    private String buildCommand(String[] args){
        StringBuilder cmd = new StringBuilder();
        for(String arg : args){
            cmd.append(arg).append(" ");
        }
        return cmd.toString();
    }

}
