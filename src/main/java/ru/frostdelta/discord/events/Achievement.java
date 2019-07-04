package ru.frostdelta.discord.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import ru.looprich.discordlogger.modules.DiscordBot;

@Deprecated
public class Achievement implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerAdvancementDoneEvent(PlayerAdvancementDoneEvent event) {
        DiscordBot.sendMessageChannel(event.getPlayer().getName() + " has made the new achievement!");
    }

}
