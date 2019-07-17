package ru.frostdelta.discord;

import org.bukkit.ChatColor;
import java.util.ArrayList;
import java.util.List;

public class Util {

    /*public static NPC getFakePlayerNPC(String name){

        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        NPC npc = registry.createNPC(EntityType.PLAYER, name);
        npc.setProtected(true);
        npc.setName(name);
        new SyncTasks(npc, Task.SPAWN).runTask(DiscordLogger.getInstance());
        //npc.setBukkitEntityType(EntityType.PLAYER);
        return npc;
    }*/

    public static String removeCodeColors(String message) {
        return ChatColor.stripColor(message);
    }

    public static List<String> removeCodeColors(String[] messages) {
        List<String> message = new ArrayList<>();
        for (String msg : messages)
            message.add(ChatColor.stripColor(msg));
        return message;
    }

    public static String cancelFormatMessage(String message) {
        String[] array = message.split(" ");
        StringBuilder msg = new StringBuilder();
        int pos1, pos2, difference;
        for (int i = 0; i <= array.length - 1; i++) {
            StringBuilder buff = new StringBuilder(" " + array[i]);

            pos1 = buff.toString().indexOf('_');
            pos2 = buff.toString().lastIndexOf('_');
            difference = Math.max(pos1, pos2) - Math.min(pos1, pos2);
            if (pos1 != pos2 && difference != 1) buff.insert(pos1, "\\");

            pos1 = buff.toString().indexOf('*');
            pos2 = buff.toString().lastIndexOf('*');
            difference = Math.max(pos1, pos2) - Math.min(pos1, pos2);
            if (pos1 != pos2 && difference != 1) buff.insert(pos1, "\\");

            pos1 = buff.toString().indexOf('~');
            pos2 = buff.toString().lastIndexOf('~');
            difference = Math.max(pos1, pos2) - Math.min(pos1, pos2);
            if (pos1 != pos2 && difference != 1) buff.insert(pos1, " \\");

            msg.append(buff.toString());
        }
        return msg.toString();
    }

}
