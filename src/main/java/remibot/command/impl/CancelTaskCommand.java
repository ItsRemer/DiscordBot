package remibot.command.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import remibot.EventcordBot;
import remibot.command.Command;
import remibot.mongodb.DiscordUser;
import remibot.mongodb.DiscordUserAccess;

public final class CancelTaskCommand implements Command {

    @Override
    public void handle(String[] args, MessageReceivedEvent event) {
        final DiscordUserAccess discordUserAccess = EventcordBot.mongoDB.getDiscordUserAccess();
        final DiscordUser discordUser = discordUserAccess.findByDiscordId(event.getAuthor().getId());

        if (discordUser == null) {
            event.getChannel().sendMessage("You need to set your username first by doing !setname <username> <accountType>").queue();
            return;
        }

        if (discordUser.getSlayerTask() == null) {
            event.getChannel().sendMessage("You don't have any task to cancel.").queue();
            return;
        }

        discordUser.setSlayerTask(null);
        discordUserAccess.replaceOne(discordUser);

        event.getChannel().sendMessage("Your task has been cancelled.").queue();
    }
}
