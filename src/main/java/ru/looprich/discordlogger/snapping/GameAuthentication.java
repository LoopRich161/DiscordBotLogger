package ru.looprich.discordlogger.snapping;


import net.dv8tion.jda.core.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.looprich.discordlogger.DiscordLogger;
import ru.looprich.discordlogger.modules.DiscordBot;

import java.util.Random;

public class GameAuthentication {

    private Player player = null;
    private String playerName;
    private User user;
    private Random random = new Random();
    private String code;
    private boolean confirm;

    public GameAuthentication(User user, String playerName) {
        this.playerName = playerName;
        this.user = user;
        DiscordLogger.getInstance().verifyUsers.add(this);
    }

    private void sendCode() {
        String[] allLetter = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J",
                "K", "L", "Z", "X", "C", "V", "B", "N", "M", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        StringBuilder newCode = new StringBuilder();
        for (int i = 1; i < 7; i++)
            newCode.append(allLetter[random.nextInt(allLetter.length) + 1]);

        code = newCode.toString();
        user.openPrivateChannel().queue((channel) ->
                channel.sendMessage("Код подтверждения: " + code + "." +
                        "\nВы или кто-то за Вас, пытались связать аккаунты Discord и Minecraft." +
                        "\nЕсли это не Вы обратитесь к администрации сервера!").queue());

        DiscordBot.sendVerifyMessage("Код подтверждения отправлен!" +
                "\nЕсли код не пришел необходимо открыть личные сообщения серверу.");

        confirm = false;

    }

    public void regPlayer() {
        for (Player var1 : Bukkit.getOnlinePlayers()) {
            if (playerName.equalsIgnoreCase(var1.getName())) {
                player = var1;
                break;
            }
        }
        if (player == null) {
            DiscordBot.sendVerifyMessage("Игрок оффлайн!");
            return;
        }
        if (DiscordLogger.getInstance().getNetwork().existPlayer(player)) {
            DiscordBot.sendVerifyMessage("Игрок " + player.getName() + " уже связал свои аккаунты!");
            return;
        }
        if (DiscordLogger.getInstance().getNetwork().existUser(user)) {
            DiscordBot.sendVerifyMessage("Пользователь " + user.getAsTag() + " уже связал свои аккаунты!");
            return;
        }
        AuthenticationTimer timer = new AuthenticationTimer(this);
        sendCode();
        player.sendMessage(ChatColor.GOLD + "Вы или кто-то за Вас, пытались связать аккаунты Discord и Minecraft");
        player.sendMessage(ChatColor.AQUA + "/verify accept <code>" + ChatColor.GREEN + "- принять предложение");
        player.sendMessage(ChatColor.AQUA + "/verify reject " + ChatColor.GREEN + "- отклонить предложение");
        timer.runTaskTimer(DiscordLogger.getInstance(), 0L, 20L);
    }


    public void reject() {
        DiscordBot.sendVerifyMessage("Игрок " + player.getName() + " отказался связать свои аккаунты!");
        DiscordLogger.getInstance().verifyUsers.remove(this);
    }

    public void accept() {
        confirm = true;
        DiscordBot.sendVerifyMessage("Игрок " + player.getName() + " связал свои аккаунты!");
        DiscordLogger.getInstance().getNetwork().verifyPlayer(player, user, code);
        DiscordLogger.getInstance().verifyUsers.remove(this);
    }

    public void fail() {
        DiscordBot.sendVerifyMessage("Игрок " + player.getName() + " не успел подтвердить код!");
        DiscordLogger.getInstance().verifyUsers.remove(this);
    }

    public boolean isConfirm() {
        return confirm;
    }

    public Player getPlayer() {
        return player;
    }

    public String getPlayerName() {
        return playerName;
    }

}
