package ru.frostdelta.discord;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import ru.looprich.discordlogger.modules.DiscordBot;

public class RemoteConfigControl extends ListenerAdapter {

    public void onGuildMessageReceivedEvent(GuildMessageReceivedEvent event){
        String [] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(DiscordBot.prefix+"help")){
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Помощь");
            info.setDescription("Просмотрев данную информацию станет легче что-то там.");
            info.addField("Creator","LoopRich161, FrostDelta123",false);
            info.setColor(0xf45642);

            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(info.build()).queue();
            info.clear();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //TODO listening commands from discord channel, config editing
    }

}
