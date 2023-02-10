package remibot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {
    void handle(String[] args, MessageReceivedEvent event);
}
