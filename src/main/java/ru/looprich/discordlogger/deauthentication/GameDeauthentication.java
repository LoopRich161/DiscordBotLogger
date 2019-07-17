package ru.looprich.discordlogger.deauthentication;

import net.dv8tion.jda.core.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.modules.DiscordBot;

import java.util.Random;

public class GameDeauthentication {
    private final String[] allLetter = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J",
            "K", "L", "Z", "X", "C", "V", "B", "N", "M", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    private final Random random = new Random();
    private String playerName;
    private Player player = null;
    private User user;
    private String code;
    private boolean confirm;

    public GameDeauthentication(String player, User user) {
        this.playerName = player;
        this.user = user;
    }

    private void sendCode() {
        StringBuilder newCode = new StringBuilder();
        for (int i = 1; i < 7; i++)
            newCode.append(allLetter[random.nextInt(allLetter.length)]);
        code = newCode.toString();
        user.openPrivateChannel().queue((channel) ->
                channel.sendMessage("Код подтверждения: " + code).queue());
        player.sendMessage(ChatColor.GREEN + "Код подтверждения отправлен!" +
                "\nЕсли код подтверждения не пришел, необходимо разрешить личные сообщения от участников сервера.");
        confirm = false;
    }

    public void deauthentication() {
        for (Player var1 : Bukkit.getOnlinePlayers()) {
            if (var1.getName().equalsIgnoreCase(playerName)) {
                player = var1;
                break;
            }
        }
        if (player == null) {
            DiscordBot.sendVerifyMessage("Игрок оффлайн!");
            return;
        }
        if (!DiscordLogger.getInstance().getNetwork().existPlayer(player, user)) {
            player.sendMessage(ChatColor.RED + "Вы ещё не привязали свой Minecraft аккаунт к Discord или Ваши аккаунты уже привязаны к другим учетным записям!");
            return;
        }
        if (!DiscordLogger.getInstance().getNetwork().existUser(user, player)) {
            player.sendMessage(ChatColor.RED + "Вы ещё не привязали свой Discord аккаунт к Minecraft или Ваши аккаунты уже привязаны к другим учетным записям!");
            return;
        }
        sendCode();
        DeauthenticationTimer timer = new DeauthenticationTimer(this);
        user.openPrivateChannel().queue((channel) ->
                channel.sendMessage("Вы или кто-то за Вас, пытаетесь отвязать свой Minecraft аккаунт от Discord." +
                        "\nЕсли это не Вы обратитесь к администрации сервера!").queue());
        player.sendMessage("Вы или кто-то за Вас, пытаетесь отвязать свой Minecraft аккаунт от Discord." +
                "\nЕсли это не Вы обратитесь к администрации сервера!");
        DiscordLogger.getInstance().gameDeauthenticationPlayers.add(this);
        timer.runTaskTimer(DiscordLogger.getInstance(), 0L, 20L);
    }


    public void reject() {
        confirm = true;
        user.openPrivateChannel().queue((channel) ->
                channel.sendMessage("Вы отказались отвязать аккаунты Minecraft и Discord или ввели неверный код подтверждения.").queue());
        DiscordBot.sendVerifyMessage("Игрок " + player.getName() + " отказался отвязать свои Minecraft и Discord аккаунты или ввел неверный код подтверждения!");
        DiscordLogger.getInstance().gameDeauthenticationPlayers.remove(this);
    }

    public void accept() {
        confirm = true;
        DiscordBot.sendVerifyMessage("Игрок " + player.getName() + " согласился отвязать свой Minecraft аккаунт и Discord!");
        player.sendMessage(ChatColor.GREEN + "Вы согласились отвязать свой Minecraft от Discord аккаунта!");
        DiscordLogger.getInstance().getNetwork().deauthentication(player);
        DiscordLogger.getInstance().gameDeauthenticationPlayers.remove(this);
    }

    void fail() {
        DiscordBot.sendVerifyMessage("Игрок " + player.getName() + " не успел подтвердить код в игре!");
        player.sendMessage(ChatColor.RED + "Вы не успели подтвердить код!");
        DiscordLogger.getInstance().gameDeauthenticationPlayers.remove(this);
    }

    boolean isConfirm() {
        return confirm;
    }

    public String getPlayerName() {
        return player.getName();
    }

    public String getCode() {
        return code;
    }

    public User getUser() {
        return user;
    }

}
