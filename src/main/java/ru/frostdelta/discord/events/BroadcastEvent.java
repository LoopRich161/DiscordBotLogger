package ru.frostdelta.discord.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.BroadcastMessageEvent;

import static ru.looprich.discordlogger.modules.DiscordBot.sendMessage;

public class BroadcastEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void broadcast(BroadcastMessageEvent event) {
        sendMessage("[BROADCAST]<" + event.getRecipients().toString() + "> " + event.getMessage());
    }

}
