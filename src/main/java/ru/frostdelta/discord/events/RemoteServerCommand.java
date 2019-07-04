package ru.frostdelta.discord.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.RemoteServerCommandEvent;
import ru.looprich.discordlogger.modules.DiscordBot;

public class RemoteServerCommand implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void onRemoteServerCommandEvent(RemoteServerCommandEvent event) {
        DiscordBot.sendMessageChannel(event.getSender().getName() + " issued server command: " + event.getCommand());
    }

}
