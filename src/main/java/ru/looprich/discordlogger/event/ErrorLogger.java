package ru.looprich.discordlogger.event;


import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

public class ErrorLogger extends AbstractAppender {
    public ErrorLogger(Appender appender) {
        super(appender.getName(), null, appender.getLayout(), false);
    }

    @Override
    public void append(LogEvent logEvent) {
        //TODO wait test
        System.out.println("logEvent.getLevel(): " + logEvent.getLevel());
        System.out.println("logEvent.getMessage(): " + logEvent.getMessage());
        System.out.println("logEvent.getThrown().getMessage(): " + logEvent.getThrown().getMessage());
        System.out.println("logEvent.getContextData().toString():" + logEvent.getContextData().toString());
    }


}
