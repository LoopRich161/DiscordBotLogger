package ru.looprich.discordlogger.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;
import ru.frostdelta.discord.Util;
import ru.looprich.discordlogger.modules.DiscordBot;

public class EventHandlers implements Listener {

    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        DiscordBot.sendMessageChannel(event.getPlayer().getName() + " lost connection: Disconnected");
        DiscordBot.sendMessageChannel(event.getPlayer().getName() + " left the game");
    }

    public void onBroadcastMessageEvent(BroadcastMessageEvent event) {
        DiscordBot.sendMessageChannel("[BROADCAST] " + Util.removeCodeColors(event.getMessage()));
    }

    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        DiscordBot.sendMessageChannel(event.getPlayer().getName() + " issued server command: " + Util.removeCodeColors(event.getMessage()));
    }

    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        DiscordBot.sendMessageChannel(player.getName() + "[" + player.getAddress().getAddress() + ":" + player.getAddress().getPort() + "] logged at (["
                + player.getLocation().getWorld().getName() + "]"
                + player.getLocation().getX() + ", "
                + player.getLocation().getY() + ", "
                + player.getLocation().getZ() + ")");
    }

    public void onServerCommandEvent(ServerCommandEvent event) {
        DiscordBot.sendMessageChannel(event.getSender().getName() + " issued server command: " + event.getCommand());
    }

    public void onRemoteServerCommandEvent(RemoteServerCommandEvent event) {
        DiscordBot.sendMessageChannel(event.getSender().getName() + " issued server command: " + event.getCommand());
    }

    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        DiscordBot.sendMessageChannel("UUID of player " + event.getPlayer().getName() + " is " + event.getPlayer().getUniqueId());
    }

    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;
        if (event.getRecipients().size() == Bukkit.getOnlinePlayers().size())
            DiscordBot.sendMessageChannel("[G]<" + event.getPlayer().getName() + "> " + Util.removeCodeColors(event.getMessage().replaceFirst("!", "")));
        else if (DiscordBot.isLocalEnabled())
            DiscordBot.sendMessageChannel("[L]<" + event.getPlayer().getName() + "> " + Util.removeCodeColors(event.getMessage()));
    }

    public void onPlayerAchievementAwardedEvent(PlayerAchievementAwardedEvent event) {
        DiscordBot.sendMessageChannel(event.getPlayer().getName() + " has made the new achievement!");
    }

}
