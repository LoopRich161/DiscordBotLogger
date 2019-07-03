package ru.frostdelta.discord.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import ru.looprich.discordlogger.modules.DiscordBot;

public class Achievement implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerAdvancementDoneEvent(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        DiscordBot.sendMessageChannel(player.getName() + " has made the new achievement!");
    }

}
