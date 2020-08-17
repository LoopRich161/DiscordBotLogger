package ru.looprich.discordlogger.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;
import ru.frostdelta.discord.Util;
import ru.looprich.discordlogger.module.DiscordBot;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EventListener implements Listener {

    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        DiscordBot.sendMessageChannel(event.getPlayer().getName() + " left the game");
    }

    public void onBroadcastMessageEvent(BroadcastMessageEvent event) {
        DiscordBot.sendMessageChannel("[BROADCAST] " + Util.removeCodeColors(event.getMessage()));
    }

    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        if (Util.checkContains(event.getMessage())) return;
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
        if (Util.checkContains(event.getCommand())) return;
        DiscordBot.sendMessageChannel(event.getSender().getName() + " issued server command: " + event.getCommand());
    }

    public void onRemoteServerCommandEvent(RemoteServerCommandEvent event) {
        if (Util.checkContains(event.getCommand())) return;
        DiscordBot.sendMessageChannel(event.getSender().getName() + " issued server command: " + event.getCommand());
    }

    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        DiscordBot.sendMessageChannel("UUID of player " + event.getPlayer().getName() + " is " + event.getPlayer().getUniqueId());
    }

    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;
        if (Bukkit.getServer().getPluginManager().getPlugin("KroChat") != null) {
            if (event.getMessage().startsWith("!")) {
                DiscordBot.sendMessageChannel("[G]<" + event.getPlayer().getName() + "> " + Util.removeCodeColors(event.getMessage().replaceFirst("!", "")));
            } else if (DiscordBot.isLocalEnabled()) {
                DiscordBot.sendMessageChannel("[L]<" + event.getPlayer().getName() + "> " + Util.removeCodeColors(event.getMessage()));
            }
        } else {
            if (event.getRecipients().size() == Bukkit.getOnlinePlayers().size()) {
                DiscordBot.sendMessageChannel("[G]<" + event.getPlayer().getName() + "> " + Util.removeCodeColors(event.getMessage().replaceFirst("!", "")));
            } else if (DiscordBot.isLocalEnabled()) {
                DiscordBot.sendMessageChannel("[L]<" + event.getPlayer().getName() + "> " + Util.removeCodeColors(event.getMessage()));
            }
        }

    }

    public void onPlayerAdvancementDoneEvent(PlayerAdvancementDoneEvent event) {
        try {
            Object craftAdvancement = ((Object) event.getAdvancement()).getClass().getMethod("getHandle").invoke(event.getAdvancement());
            Object advancementDisplay = craftAdvancement.getClass().getMethod("c").invoke(craftAdvancement);
            boolean display = (boolean) advancementDisplay.getClass().getMethod("i").invoke(advancementDisplay);
            if (!display) return;
        } catch (NullPointerException e) {
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String rawAdvancementName = event.getAdvancement().getKey().getKey();
        String advancementName = Arrays.stream(rawAdvancementName.substring(rawAdvancementName.lastIndexOf("/") + 1).toLowerCase().split("_"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining(" "));

        DiscordBot.sendMessageChannel(event.getPlayer().getName() + " has made the achievement [" + advancementName + "]");
    }

}
