package ru.looprich.discordlogger.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import ru.looprich.discordlogger.modules.DiscordBot;

import static ru.looprich.discordlogger.modules.DiscordBot.sendMessage;


public class EventHandlers implements Listener {



    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerAchievementAwardedEvent(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        sendMessage(player.getName() + " has made the advancement!");
    }
}
