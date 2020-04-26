package ru.frostdelta.discord.task;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import ru.frostdelta.discord.fake.FakeConsoleSender;
import ru.frostdelta.discord.fake.FakePlayer;
import ru.frostdelta.discord.fake.FakePlayerCommandSender;
import ru.looprich.discordlogger.module.DiscordBot;

import java.util.UUID;

public class SyncTasks extends BukkitRunnable {

    private final Task task;
    private String command;
    private FakePlayerCommandSender commandSender;
    private String message;
    private String fakePlayerName;
    private UUID fakePlayerUUID;


    public SyncTasks(FakePlayerCommandSender sender, String command, Task task) {
        this.commandSender = sender;
        this.command = command;
        this.task = task;
    }

    public SyncTasks(String fakePlayerName, String message, Task task) {
        this.fakePlayerName = fakePlayerName;
        this.task = task;
        this.message = message;
    }

    public SyncTasks(UUID uuid, String message, Task task) {
        this.fakePlayerUUID = uuid;
        this.task = task;
        this.message = message;
    }

    @Override
    public void run() {
        switch (task) {
            case DISPATCH:
                Bukkit.dispatchCommand(new FakeConsoleSender(), command);
                DiscordBot.sendImportantMessage(commandSender.getName() + " issued server command: " + command);
                break;
            case SPAWN:
                //npc.spawn(new Location(Bukkit.getWorld("world"), 0,0,0));
                break;
            case COMMAND:
                Bukkit.dispatchCommand(commandSender, command);
                break;
            case CHAT:
                new FakePlayer(fakePlayerUUID).chat(message);
                break;
            case UNKNOWN:
                break;
        }

    }
}
