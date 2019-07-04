package ru.frostdelta.discord.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import ru.looprich.discordlogger.modules.DiscordBot;

public class ServerCommand implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void onServerCommandEvent(ServerCommandEvent event) {
        DiscordBot.sendMessageChannel(event.getSender().getName() + " issued server command: " + event.getCommand());
    }

}
