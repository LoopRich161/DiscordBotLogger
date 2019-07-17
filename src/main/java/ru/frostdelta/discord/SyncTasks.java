package ru.frostdelta.discord;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class SyncTasks extends BukkitRunnable {

    private NPC npc;
    private Task

    public SyncTasks(NPC npc){
        this.npc = npc;
    }

    @Override
    public void run() {

        npc.spawn(new Location(Bukkit.getWorld("world"), 0,0,0));
    }


}
