package remibot.command.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import remibot.command.Command;

public final class ClearCommand implements Command {

    @Override
    public void handle(String[] args, MessageReceivedEvent event) {
        event.getMessage().getChannel().getIterableHistory().stream()
                .filter(m -> m.getAuthor().equals(event.getMessage().getJDA().getSelfUser()))
                .forEach(m -> m.delete().queue());
    }
}
