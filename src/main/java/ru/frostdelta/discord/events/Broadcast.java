package ru.frostdelta.discord.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.BroadcastMessageEvent;
import ru.looprich.discordlogger.modules.DiscordBot;

public class Broadcast implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void onBroadcastMessageEvent(BroadcastMessageEvent event) {
        DiscordBot.sendMessageChannel("[BROADCAST]<" + event.getRecipients().toString() + "> " + event.getMessage());
    }

}
