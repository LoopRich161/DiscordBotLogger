package ru.frostdelta.discord.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import ru.frostdelta.discord.Util;
import ru.looprich.discordlogger.modules.DiscordBot;

public class PlayerCommandPreprocess implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        DiscordBot.sendMessageChannel(event.getPlayer().getName() + " issued server command: " + Util.removeCodeColors(event.getMessage()));
    }


}
