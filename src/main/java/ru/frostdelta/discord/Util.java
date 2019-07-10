package ru.frostdelta.discord;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static String removeCodeColors(String message){
        return ChatColor.stripColor(message);
    }

    public static List<String> removeCodeColors(String[] messages){
        List<String> message = new ArrayList<>();
        for(String msg : messages){
            message.add(ChatColor.stripColor(msg));
        }
        return message;
    }
}
