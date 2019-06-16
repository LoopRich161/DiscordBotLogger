package ru.frostdelta.discord.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

import static ru.looprich.discordlogger.modules.DiscordBot.sendMessage;

public class AchievementEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void achivement(PlayerAchievementAwardedEvent event) {
        Player player = event.getPlayer();
        sendMessage(player.getName() + " has made the achievement " + event.getAchievement().name());
    }

}
