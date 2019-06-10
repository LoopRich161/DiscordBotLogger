package ru.looprich.discordlogger.modules;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    private String tokenBot;
    private String channel;
    private TextChannel loggerChannel = null;
    private JDA jda = null;

    public DiscordBot(String tokenBot, String channel) {
        this.tokenBot = tokenBot;
        this.channel = channel;
    }

    public boolean createBot() {
        try {
            jda = new JDABuilder(tokenBot).build().awaitReady();
        } catch (LoginException e) {
            System.out.println("Invalid token!");
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loggerChannel = jda.getTextChannelById(channel);
        return loggerChannel != null;
    }

    public void sendMessageChannel(String message) {
        loggerChannel.sendMessage(message).queue();
    }

    public JDA getJDA() {
        return jda;
    }
}
