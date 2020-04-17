package ru.looprich.discordlogger.event;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.util.Throwables;
import ru.looprich.discordlogger.module.DiscordBot;


public class ErrorLogger extends AbstractAppender {
    public ErrorLogger(Appender consoleAppender) {
        super("ErrorLogger", null, consoleAppender.getLayout(), false);
    }

    @Override
    public void append(LogEvent logEvent) {
        if (logEvent.getLevel().equals(Level.ERROR)) {
            //todo
            StringBuilder message = new StringBuilder(logEvent.getMessage().getFormattedMessage());
            Throwable throwable = logEvent.getThrown();
            if (throwable != null) {
                for (String s : Throwables.toStringList(throwable)) {
                    message.append("\n").append(s);
                }
                DiscordBot.sendMessageTechAdmin(message.toString());
            }
        }
    }
}
