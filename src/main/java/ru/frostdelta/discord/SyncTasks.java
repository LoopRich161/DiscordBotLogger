package ru.frostdelta.discord;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class SyncTasks extends BukkitRunnable {

    private NPC npc;
    private Task task;
    private String command;
    private FakePlayerCommandSender commandSender;
    private String message;
    private String fakePlayerName;


    public SyncTasks(FakePlayerCommandSender sender, String command, Task task){
        this.commandSender = sender;
        this.command = command;
        this.task = task;
    }

    public SyncTasks(String fakePlayerName, String message, Task task){
        this.fakePlayerName = fakePlayerName;
        this.task = task;
        this.message = message;
    }

    public SyncTasks(NPC npc, Task task){
        this.npc = npc;
        this.task = task;
    }

    @Override
    public void run() {
        switch (task){
            case SPAWN:
                npc.spawn(new Location(Bukkit.getWorld("world"), 0,0,0));
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
