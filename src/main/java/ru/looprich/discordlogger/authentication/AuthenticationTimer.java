package ru.looprich.discordlogger.authentication;

import org.bukkit.scheduler.BukkitRunnable;
import ru.looprich.discordlogger.DiscordLogger;

public class AuthenticationTimer extends BukkitRunnable {
    private int verifyTime = DiscordLogger.getInstance().getConfig().getInt("bot.authentication-time");
    private int time = 0;
    private GameAuthentication gameAuthentication;

    AuthenticationTimer(GameAuthentication gameAuthentication) {
        this.gameAuthentication = gameAuthentication;
    }

    @Override
    public void run() {
        if (time == verifyTime) {
            if (!gameAuthentication.isConfirm()) gameAuthentication.fail();
            cancel();
        }
        time++;
    }
}
