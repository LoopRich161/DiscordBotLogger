package ru.frostdelta.discord.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import static ru.looprich.discordlogger.modules.DiscordBot.sendMessageChannel;

public class PlayerQuitEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void quit(org.bukkit.event.player.PlayerQuitEvent event) {
        Player player = event.getPlayer();
        sendMessageChannel(player.getName() + " lost connection: Disconnected");
        sendMessageChannel(player.getName() + " left the game");
    }

}
