package ru.looprich.discordlogger.deauthentication;

import org.bukkit.scheduler.BukkitRunnable;
import ru.looprich.discordlogger.DiscordLogger;

public class DeauthenticationTimer extends BukkitRunnable {
    private int deauthenticationTime = DiscordLogger.getInstance().getConfig().getInt("bot.deauthentication-time");
    private int time = 0;
    private GameDeauthentication gameDeauthentication;

    DeauthenticationTimer(GameDeauthentication gameDeauthentication) {
        this.gameDeauthentication = gameDeauthentication;
    }

    @Override
    public void run() {
        if (time == deauthenticationTime) {
            if (!gameDeauthentication.isConfirm()) gameDeauthentication.fail();
            cancel();
        }
        time++;
    }
}
