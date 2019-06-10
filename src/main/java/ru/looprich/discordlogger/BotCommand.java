package ru.looprich.discordlogger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.looprich.discordlogger.modules.DiscordBot;

public class BotCommand implements CommandExecutor {

    static void reg(){
        Core.getInstance().getCommand("bot").setExecutor(new BotCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1){
            switch (args[0]){
                case "enable":
                    if(!DiscordBot.getBot().isEnabled()) {
                        Core.getInstance().loadDiscordBot();
                        System.out.println("Bot successful enabled!");
                    } else System.out.println("Bot already enabled!");
                    break;
                case "disable":
                    DiscordBot.getBot().getJDA().shutdownNow();
                    System.out.println("Bot successful disabled!");
                    break;
                case "restart":
                    if(DiscordBot.getBot().isEnabled()) {
                        DiscordBot.getBot().getJDA().shutdownNow();
                        Core.getInstance().loadDiscordBot();
                        System.out.println("Bot successful restarted!");
                    } else {
                        Core.getInstance().loadDiscordBot();
                        System.out.println("Bot successful restarted!");
                    }
                    break;
            }
        }else sender.sendMessage(ChatColor.RED + "Command usage: " + ChatColor.GOLD + "/bot <enable/disable/restart>");
        return true;
    }

}
