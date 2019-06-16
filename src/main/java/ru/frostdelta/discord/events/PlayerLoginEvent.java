package ru.frostdelta.discord.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import static ru.looprich.discordlogger.modules.DiscordBot.sendMessage;

public class PlayerLoginEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerLoginEvent(org.bukkit.event.player.PlayerLoginEvent event) {
        Player player = event.getPlayer();
        sendMessage("UUID of player " + player.getName() + " is " + player.getUniqueId());
    }

}
