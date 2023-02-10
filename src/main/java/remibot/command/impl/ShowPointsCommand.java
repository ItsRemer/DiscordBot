package remibot.command.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import remibot.EventcordBot;
import remibot.command.Command;
import remibot.mongodb.DiscordUser;
import remibot.mongodb.DiscordUserAccess;

public final class ShowPointsCommand implements Command {

    @Override
    public void handle(String[] args, MessageReceivedEvent event) {
        final DiscordUserAccess discordUserAccess = EventcordBot.mongoDB.getDiscordUserAccess();
        final DiscordUser discordUser = discordUserAccess.findByDiscordId(event.getAuthor().getId());

        if (discordUser == null || discordUser.getRunescapeUsername() == null) {
            event.getChannel().sendMessage("It seems like you do not have an account linked, please do !setname <username> <accountType>").queue();
            return;
        }
        event.getMessage().getChannel().sendMessage("You currently have " + discordUser.getSlayerPoints() + " Slayer points.").queue();

    }
}
