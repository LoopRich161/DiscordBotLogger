package ru.frostdelta.discord;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import ru.looprich.discordlogger.module.DiscordBot;

public class SyncTasks extends BukkitRunnable {

    private Task task;
    private String command;
    private FakePlayerCommandSender commandSender;
    private String message;
    private String fakePlayerName;


    SyncTasks(FakePlayerCommandSender sender, String command, Task task) {
        this.commandSender = sender;
        this.command = command;
        this.task = task;
    }

    SyncTasks(String fakePlayerName, String message, Task task) {
        this.fakePlayerName = fakePlayerName;
        this.task = task;
        this.message = message;
    }

    @Override
    public void run() {
        switch (task) {
            case DISPATCH:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                DiscordBot.sendImportantMessage(commandSender.getName() + " issued server command: " + command);
                break;
            case SPAWN:
                //npc.spawn(new Location(Bukkit.getWorld("world"), 0,0,0));
                break;
            case COMMAND:
                Bukkit.dispatchCommand(commandSender, command);
                break;
            case CHAT:
                new FakePlayer(fakePlayerName).chat(message);
                break;
            case UNKNOWN:
                break;
        }

    }
}
