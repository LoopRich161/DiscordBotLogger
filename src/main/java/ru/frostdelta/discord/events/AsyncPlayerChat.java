package ru.frostdelta.discord.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.looprich.discordlogger.modules.DiscordBot;

public class AsyncPlayerChat implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;
        if (event.getRecipients().size() == Bukkit.getOnlinePlayers().size())
            DiscordBot.sendMessageChannel("[G]<" + event.getPlayer().getName() + "> " + event.getMessage().replaceFirst("!", ""));
        else if (DiscordBot.isLocalEnabled())
            DiscordBot.sendMessageChannel("[L]<" + event.getPlayer().getName() + "> " + event.getMessage());
    }

}
