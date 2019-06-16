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
    void onPlayerLoginEvent(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        sendMessage("UUID of player " + player.getName() + " is " + player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void PlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        sendMessage(player.getName() + "[" + player.getAddress().getAddress() + ":" + player.getAddress().getPort() + "] logged at (["
                + player.getLocation().getWorld().getName() + "]"
                + player.getLocation().getX() + ", "
                + player.getLocation().getY() + ", "
                + player.getLocation().getZ() + ")");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        sendMessage(player.getName() + " lost connection: Disconnected");
        sendMessage(player.getName() + " left the game");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        sendMessage(player.getName() + " issued server command: " + event.getMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerAchievementAwardedEvent(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        sendMessage(player.getName() + " has made the advancement!");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();
        if (event.getRecipients().size() == Bukkit.getOnlinePlayers().size()) {
            sendMessage("[G]<" + player.getName() + "> " + msg.replace("!", ""));
        } else {
            if (DiscordBot.isLocalEnabled()) {
                sendMessage("[L]<" + player.getName() + "> " + msg);
            }
        }
    }
}
