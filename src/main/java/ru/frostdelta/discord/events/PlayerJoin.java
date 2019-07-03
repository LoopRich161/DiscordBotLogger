package ru.frostdelta.discord.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.looprich.discordlogger.modules.DiscordBot;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        DiscordBot.sendMessageChannel(player.getName() + "[" + player.getAddress().getAddress() + ":" + player.getAddress().getPort() + "] logged at (["
                + player.getLocation().getWorld().getName() + "]"
                + player.getLocation().getX() + ", "
                + player.getLocation().getY() + ", "
                + player.getLocation().getZ() + ")");
    }

}
