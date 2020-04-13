package ru.looprich.discordlogger.event;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;


public class ErrorLogger extends AbstractAppender {
    public ErrorLogger(Appender consoleAppender) {
        super("ErrorLogger", null, consoleAppender.getLayout(), false);
    }

    @Override
    public void append(LogEvent logEvent) {
        if (logEvent.getLevel().equals(Level.ERROR)) {
            //TODO
        }
    }
}
