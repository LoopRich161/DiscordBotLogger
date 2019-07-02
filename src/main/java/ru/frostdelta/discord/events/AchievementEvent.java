package ru.frostdelta.discord.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import static ru.looprich.discordlogger.modules.DiscordBot.sendMessageChannel;

public class AchievementEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void achivement(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        sendMessageChannel(player.getName() + " has made the new achievement!");
    }

}
