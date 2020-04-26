package ru.frostdelta.discord.bot;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.looprich.discordlogger.DiscordLogger;

public class Deauthentication {
    private final Player player;

    public Deauthentication(Player player) {
        this.player = player;
    }

    public void deauth() {
        if (searchUser()) {
            DiscordLogger.getInstance().getNetwork().deauthentication(player);
            player.sendMessage(ChatColor.GREEN + "Вы отвязали свой аккаунт от Discord!");
        } else player.sendMessage(ChatColor.RED + "Вы не привязали свой аккаунт к Discord!");
    }

    private boolean searchUser() {
        return DiscordLogger.getInstance().getNetwork().existPlayer(player);
    }


}
