package ru.frostdelta.discord;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class RemoteConfigControl extends ListenerAdapter {

    public void onGuildMessageReceivedEvent(GuildMessageReceivedEvent event){
        String [] args = event.getMessage().getContentRaw().split(" ");

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //TODO listening commands from discord channel, config editing
    }

}
