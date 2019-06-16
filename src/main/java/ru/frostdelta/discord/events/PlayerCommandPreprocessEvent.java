package ru.frostdelta.discord.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import static ru.looprich.discordlogger.modules.DiscordBot.sendMessage;

public class PlayerCommandPreprocessEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void command(org.bukkit.event.player.PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        sendMessage(player.getName() + " issued server command: " + event.getMessage());
    }


}
