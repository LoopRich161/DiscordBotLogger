package ru.looprich.discordlogger.event;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.util.Throwables;
import ru.looprich.discordlogger.module.DiscordBot;
import ru.looprich.discordlogger.module.HasteManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ErrorLogger extends AbstractAppender {
    public ErrorLogger(Appender consoleAppender) {
        super("ErrorLogger", null, consoleAppender.getLayout(), false);
    }

    @Override
    public void append(LogEvent logEvent) {
        if (logEvent.getLevel().equals(Level.ERROR)) {
            List<String> errorMessage = new ArrayList<>();
            errorMessage.add(logEvent.getMessage().getFormattedMessage());
            Throwable throwable = logEvent.getThrown();
            if (throwable != null) {
                errorMessage.addAll(Throwables.toStringList(throwable));
                try {
                    String url = HasteManager.post(errorMessage);
                    DiscordBot.sendMessageUser(DiscordBot.getTechAdmin(), "Найдена ошибка на сервере: " + url);
                } catch (IOException e) {
                    DiscordBot.sendMessageUser(DiscordBot.getTechAdmin(), "Найдена ошибка на сервере: " + errorMessage.toString()
                            .replaceAll(", ", "\n")
                            .replace("[", "").replace("]", ""));
                }

            }
        }
    }
}
