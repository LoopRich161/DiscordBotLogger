package ru.frostdelta.discord.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import static ru.looprich.discordlogger.modules.DiscordBot.sendMessageChannel;

public class PlayerJoinEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
   public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();
        sendMessageChannel(player.getName() + "[" + player.getAddress().getAddress() + ":" + player.getAddress().getPort() + "] logged at (["
                + player.getLocation().getWorld().getName() + "]"
                + player.getLocation().getX() + ", "
                + player.getLocation().getY() + ", "
                + player.getLocation().getZ() + ")");
    }

}
