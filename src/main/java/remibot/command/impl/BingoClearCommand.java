package remibot.command.impl;


import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import remibot.EventcordBot;
import remibot.command.Command;

public class BingoClearCommand implements Command {

    @Override
    public void handle(String[] args, MessageReceivedEvent event) {
        EventcordBot.mongoDB.getBingoGameAccess().softDelete(event.getGuild().getId());
        event.getChannel().sendMessage("Bingo items cleared!").queue();
    }
}
