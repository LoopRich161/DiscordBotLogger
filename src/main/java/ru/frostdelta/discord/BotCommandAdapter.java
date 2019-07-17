package ru.frostdelta.discord;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
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

            //OLD FakePlayerCommandSender
            new SyncTasks(new FakePlayer(DiscordLogger.getInstance().getNetwork().getAccountMinecraftName(event.getAuthor())), cmd, Task.COMMAND).runTask(DiscordLogger.getInstance());

            if (DiscordBot.isIsWhitelistEnabled()){
                List<String> whitelist = DiscordLogger.getInstance().getConfig().getStringList("whitelist");
                for(String allowedCmd : whitelist){
                    if (args[1].contains(allowedCmd)){

                        break;
                    }
                }
                //блеклист
                //TODO добавить парсер команд в зависимости от white/black листа
            }
        }

        if(botCommand.equalsIgnoreCase(DiscordBot.prefix + "chat")){
            if (!DiscordLogger.getInstance().getNetwork().existUser(event.getAuthor())) {
                DiscordBot.sendVerifyMessage("Вы не прошли верификацию!");
                return;
            }
            String message = buildCommand(Arrays.copyOfRange(args, 1, args.length));
            new SyncTasks(DiscordLogger.getInstance().getNetwork().getAccountMinecraftName(event.getAuthor()), message, Task.CHAT).runTask(DiscordLogger.getInstance());
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
