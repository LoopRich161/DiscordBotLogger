package ru.frostdelta.discord.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.looprich.discordlogger.modules.DiscordBot;

public class PlayerQuit implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        DiscordBot.sendMessageChannel(player.getName() + " lost connection: Disconnected\n" +
                player.getName() + " left the game");
    }

}
