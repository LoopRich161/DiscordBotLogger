package ru.looprich.discordlogger;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.looprich.discordlogger.modules.DiscordBot;

public class GameSnapping {
    private Player player = null;
    private String playerName;

    GameSnapping(String playerName) {
        this.playerName = playerName;
    }

    public void regPlayer() {
        for (Player var1 : Bukkit.getOnlinePlayers()) {
            if (playerName.equalsIgnoreCase(var1.getName())) {
                this.player = var1;
                break;
            }
        }
        if (player == null)
            DiscordLogger.getInstance().getLogger().info("Player is null.");
        if (DiscordLogger.getInstance().getNetwork().existPlayer(player)) {
            DiscordBot.sendImportantMessage("Игрок " + player.getName() + " уже привязал свой аккаунт!");
            return;
        }
        player.sendMessage(ChatColor.GOLD + "Вы или кто-то за вас, пытались связать аккаунты Discord и Minecraft");
        player.sendMessage(ChatColor.AQUA + "/bot accept " + ChatColor.GREEN + "- чтобы принять предложение");
        player.sendMessage(ChatColor.AQUA + "/bot refuse " + ChatColor.GREEN + "- чтобы отклонить предложение");
        /// TODO: 25.06.2019 add a timer after which the offer will be refuse
    }

    public void refuse() {

    }

    public void accept() {

    }


}
