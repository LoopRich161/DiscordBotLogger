package ru.looprich.discordlogger;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.looprich.discordlogger.module.DiscordBot;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Authentication {
    private User user;
    private Player player;
    private String playerName;
    private boolean canSendPrivateMsg;
    private StringBuilder code = new StringBuilder();
    private final String[] allLetter = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J",
            "K", "L", "Z", "X", "C", "V", "B", "N", "M", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    private final Random random = new Random();

    public Authentication(User user, String playerName) {
        this.user = user;
        this.playerName = playerName;
    }

    public void auth() {
        if (!searchPlayer() || !searchUser()) return;
        if (!canSendPrivateMsg) {
            DiscordBot.sendVerifyMessage(user.getAsTag() + " откройте личные сообщения!");
            return;
        }
        sendingCode();
        DiscordLogger.getInstance().getNetwork().needAuthentication(player, user, code.toString());
        DiscordLogger.getInstance().authentication.put(playerName, this);
    }

    public void reject() {
        DiscordLogger.getInstance().getNetwork().deauthentication(player);
        DiscordLogger.getInstance().authentication.remove(playerName, this);
        player.sendMessage(ChatColor.RED + "Вы отказались связать Minecraft и Discord аккаунты.");
    }

    public void accept() {
        if (!hasTime()) {
            DiscordLogger.getInstance().getNetwork().deauthentication(player);
            player.sendMessage(ChatColor.RED + "Время для подтверждения кода вышло!");
            DiscordBot.sendVerifyMessage("Время для подтверждения кода игрока " + playerName + " вышло!");
        } else {
            player.sendMessage(ChatColor.GREEN + "Вы согласились связать Minecraft и Discord аккаунты.");
            DiscordBot.sendVerifyMessage("Игрок " + playerName + " согласился связать Minecraft и Discord аккаунты.");
            DiscordLogger.getInstance().getNetwork().authentication(player);
        }
        DiscordLogger.getInstance().authentication.remove(playerName, this);
    }

    private boolean searchPlayer() {
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
            if (onlinePlayer.getName().equalsIgnoreCase(playerName)) {
                player = onlinePlayer;
                break;
            }
        if (player == null) {
            DiscordBot.sendVerifyMessage("Игрока " + playerName + " нет на сервере!");
            return false;
        }
        if (DiscordLogger.getInstance().getNetwork().existPlayer(player)) {
            DiscordBot.sendVerifyMessage("Игрок " + playerName + " уже привязал свой аккаунт!");
            return false;
        }
        return true;
    }

    private boolean searchUser() {
        if (DiscordLogger.getInstance().getNetwork().existUser(user)) {
            DiscordBot.sendVerifyMessage("У вас уже есть привязанный аккаунт Minecraft!");
            return false;
        } else return true;

    }

    private void sendMessageToUser(String message) {
        try{
            user.openPrivateChannel().queue((channel) -> {
                channel.sendMessage(message).queue();
            });
            canSendPrivateMsg = true;
        }catch (ErrorResponseException ex){
            canSendPrivateMsg = false;
        }

    }

    private void sendingCode() {
        for (int i = 1; i < 7; i++)
            code.append(allLetter[random.nextInt(allLetter.length)]);
        DiscordBot.sendVerifyMessage(user.getAsTag() + " проверьте личные сообщения. Вам выслан код подтверждения!");
        sendMessageToUser("Код подтверждения: " + code.toString() + "\n" +
                "Введите на сервере: /auth accept <code> - для завершения аутентификации.\n" +
                "Введите на сервере: /auth reject - для отказа в завершении аутентификации");
    }

    private boolean hasTime() {
        long nowTime = TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        long time = DiscordLogger.getInstance().getNetwork().getTimeAuthentication(player);
        return nowTime <= time;
    }

    public User getUser() {
        return user;
    }

    public Player getPlayer() {
        return player;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getCode() {
        return code.toString();
    }
}
