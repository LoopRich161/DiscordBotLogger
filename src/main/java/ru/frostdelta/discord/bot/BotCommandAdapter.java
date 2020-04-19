package ru.frostdelta.discord.bot;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import ru.frostdelta.discord.SyncTasks;
import ru.frostdelta.discord.Task;
import ru.frostdelta.discord.Util;
import ru.frostdelta.discord.fake.FakePlayer;
import ru.frostdelta.discord.fake.FakePlayerCommandSender;
import ru.frostdelta.discord.fake.FakePlayerPermissionManager;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.module.DiscordBot;
import ru.looprich.discordlogger.module.LogsManager;

import java.util.Arrays;
import java.util.List;

public class BotCommandAdapter extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //НЕБО УРОНИТ, НОЧЬ НА ЛАДОНИ. НАС НЕ ДОГОНЯТ
        if (!event.getChannelType().equals(ChannelType.TEXT)||!event.getTextChannel().getId().equalsIgnoreCase(DiscordBot.channel)) return;

        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args.length < 1) {
            return;
        }
        String botCommand = args[0];
        if (botCommand.equalsIgnoreCase(DiscordBot.prefix + "command")) {
            if (!DiscordLogger.getInstance().getNetwork().verifyUser(event.getAuthor())) {
                DiscordBot.sendVerifyMessage("Вы не прошли аутентификацию!");
                return;
            }
            String cmd = Util.buildCommand(Arrays.copyOfRange(args, 1, args.length));

            List<String> blacklist = DiscordLogger.getInstance().getConfig().getStringList("blacklist");
            for (String disallowedCmd : blacklist) {
                if (args[1].contains(disallowedCmd)) {
                    DiscordBot.sendServerResponse("Данная комманда запрещена!");
                    return;
                }
            }
            if (DiscordBot.isIsWhitelistEnabled()) {
                List<String> whitelist = DiscordLogger.getInstance().getConfig().getStringList("whitelist");
                for (String allowedCmd : whitelist) {
                    if (args[1].contains(allowedCmd)) {
                        new SyncTasks(new FakePlayer(DiscordLogger.getInstance().getNetwork().getAccountMinecraftName(event.getAuthor())), cmd, Task.COMMAND).runTask(DiscordLogger.getInstance());
                        return;
                    }
                }
                DiscordBot.sendServerResponse("Данная комманда запрещена!");
            } else
                new SyncTasks(new FakePlayer(DiscordLogger.getInstance().getNetwork().getAccountMinecraftName(event.getAuthor())), cmd, Task.COMMAND).runTask(DiscordLogger.getInstance());
            DiscordLogger.getInstance().getLogger().info("<" + event.getAuthor().getAsTag() + "> issued discord command: "+ DiscordBot.prefix + "command "+cmd);
        }

        if (botCommand.equalsIgnoreCase(DiscordBot.prefix + "dispatch")) {
            if (!DiscordLogger.getInstance().getNetwork().existUser(event.getAuthor())) {
                DiscordBot.sendVerifyMessage("Вы не прошли аутентификацию!");
                return;
            }
            Permission permission = FakePlayerPermissionManager.getFakePlayerPermissions();
            String playerName = DiscordLogger.getInstance().getNetwork().getAccountMinecraftName(event.getAuthor());
            boolean access = permission.playerHas(Bukkit.getServer().getWorlds().get(0).getName(), Bukkit.getOfflinePlayer(playerName), Util.getPermission());
            if(access){
                if(args.length <= 1){
                    DiscordBot.sendImportantMessage("Использование комманды - "+DiscordBot.prefix+"dispatch <command>");
                }
                String cmd = Util.buildCommand(Arrays.copyOfRange(args, 1, args.length));
                new SyncTasks(new FakePlayerCommandSender(playerName), cmd, Task.DISPATCH).runTask(DiscordLogger.getInstance());
                DiscordLogger.getInstance().getLogger().info("<" + event.getAuthor().getAsTag() + "> issued discord command: "+DiscordBot.prefix + "dispatch "+cmd);
            }
        }

        if (botCommand.equalsIgnoreCase(DiscordBot.prefix + "chat")) {
            if (!DiscordLogger.getInstance().getNetwork().existUser(event.getAuthor())) {
                DiscordBot.sendVerifyMessage("Вы не прошли аутентификацию!");
                return;
            }
            String message = Util.buildCommand(Arrays.copyOfRange(args, 1, args.length));
            new SyncTasks(DiscordLogger.getInstance().getNetwork().getAccountMinecraftUUID(event.getAuthor()), message, Task.CHAT).runTask(DiscordLogger.getInstance());
            DiscordLogger.getInstance().getLogger().info("<" + event.getAuthor().getAsTag() + "> issued discord command: " + DiscordBot.prefix + "chat " + message);

        }

        if (botCommand.equalsIgnoreCase(DiscordBot.prefix + "logs")) {
            if (!DiscordLogger.getInstance().getNetwork().existUser(event.getAuthor())) {
                DiscordBot.sendVerifyMessage("Вы не прошли аутентификацию!");
                return;
            }
            if (!Util.isDate(args[1]) && !args[1].equalsIgnoreCase("latest")) {
                DiscordBot.sendServerResponse("Вы указали не верный тип даты \n " + DiscordBot.prefix + "logs <date> - *получить логи сервера (Если указать latest, то вышлется latest.log; Дата в формате yyyy-mm-dd)*");
                return;
            }
            //todo проверка на тех.админа из конфига
            LogsManager.getLogFile(args[1]);
            DiscordLogger.getInstance().getLogger().info("<" + event.getAuthor().getAsTag() + "> issued discord command: " + DiscordBot.prefix + "logs " + args[1]);
        }
    }

}
