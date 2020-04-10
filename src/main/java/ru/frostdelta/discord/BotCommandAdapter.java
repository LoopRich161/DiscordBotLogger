package ru.frostdelta.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.module.DiscordBot;

import java.util.Arrays;
import java.util.List;

public class BotCommandAdapter extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //НЕБО УРОНИТ, НОЧЬ НА ЛАДОНИ. НАС НЕ ДОГОНЯТ
        if (!event.getTextChannel().getId().equalsIgnoreCase(DiscordBot.channel)) return;

        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args.length == 0) {
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
                        break;
                    }
                }
            } else
                new SyncTasks(new FakePlayer(DiscordLogger.getInstance().getNetwork().getAccountMinecraftName(event.getAuthor())), cmd, Task.COMMAND).runTask(DiscordLogger.getInstance());
        }

        if (botCommand.equalsIgnoreCase(DiscordBot.prefix + "dispatch")) {
            Permission permission = FakePlayerPermissionManager.getFakePlayerPermissions();
            String playerName = DiscordLogger.getInstance().getNetwork().getAccountMinecraftName(event.getAuthor());
            boolean access = permission.playerHas(Bukkit.getServer().getWorlds().get(0).getName(), Bukkit.getOfflinePlayer(playerName), Util.getPermission());
            if(access){
                if(args.length <= 1){
                    DiscordBot.sendImportantMessage("Использование комманды - ~dispatch <command>");
                }
                String cmd = Util.buildCommand(Arrays.copyOfRange(args, 1, args.length));
                new SyncTasks(playerName, cmd, Task.DISPATCH).runTask(DiscordLogger.getInstance());
            }
        }

        if (botCommand.equalsIgnoreCase(DiscordBot.prefix + "chat")) {
            if (!DiscordLogger.getInstance().getNetwork().existUser(event.getAuthor())) {
                DiscordBot.sendVerifyMessage("Вы не прошли аутентификацию!");
                return;
            }
            String message = Util.buildCommand(Arrays.copyOfRange(args, 1, args.length));
            new SyncTasks(DiscordLogger.getInstance().getNetwork().getAccountMinecraftName(event.getAuthor()), message, Task.CHAT).runTask(DiscordLogger.getInstance());
        }
    }

}
