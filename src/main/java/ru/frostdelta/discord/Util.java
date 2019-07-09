package ru.frostdelta.discord;

import org.bukkit.ChatColor;

public class Util {

    public static String removeCodeColors(String message){
        return ChatColor.stripColor(message);
    }

}
