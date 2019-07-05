package ru.looprich.discordlogger.verification;

import org.bukkit.scheduler.BukkitRunnable;
import ru.looprich.discordlogger.DiscordLogger;

public class AuthenticationTimer extends BukkitRunnable {
    private int verifyTime = DiscordLogger.getInstance().getConfig().getInt("bot.verify-time");
    private int time = 0;
    private GameAuthentication snapping;

    AuthenticationTimer(GameAuthentication snapping) {
        this.snapping = snapping;
    }

    @Override
    public void run() {
        if (time == verifyTime) {
            if (!snapping.isConfirm()) snapping.fail();
            cancel();
        }
        time++;
    }
}
