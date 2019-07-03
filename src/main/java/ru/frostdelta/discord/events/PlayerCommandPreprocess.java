package ru.frostdelta.discord.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import ru.looprich.discordlogger.modules.DiscordBot;

public class PlayerCommandPreprocess implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        DiscordBot.sendMessageChannel(player.getName() + " issued server command: " + event.getMessage());
    }


}
