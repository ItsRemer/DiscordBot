package remibot.command.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import remibot.EventcordBot;
import remibot.command.Command;
import remibot.mongodb.DiscordUser;
import remibot.mongodb.DiscordUserAccess;

public final class SetUsernameCommand implements Command {
    private static final String USAGE = "Wrong format. Please type !setname <username> and <account type = N, IM, UIM, HCIM>";
    private static final String USERNAME_EXISTS = "You have already set a username.";
    private static final String USERNAME_IN_USE = "Username already in use, please choose a different username.";
    private static final String SUCCESS = "You have successfully linked your OSRS username to your discord.";

    @Override
    public void handle(String[] args, MessageReceivedEvent event) {
        if (!validateArgs(args, event)) {
            return;
        }

        final String playerName = args[1];
        final String accountType = args[2].toUpperCase();
        final DiscordUserAccess discordUserAccess = EventcordBot.mongoDB.getDiscordUserAccess();
        final DiscordUser discordUser = discordUserAccess.findByDiscordId(event.getAuthor().getId());

        if (discordUser == null) {
            discordUserAccess.createNew(event.getAuthor().getId(), playerName, accountType);
            event.getMessage().reply(SUCCESS).queue();
            return;
        }

        if (!validateUsernameNotExists(discordUser, event)) {
            return;
        }

        if (!validateUsernameNotInUse(playerName, discordUserAccess, event)) {
            return;
        }

        discordUser.setRunescapeUsername(playerName);
        discordUser.setAccountType(accountType);
        discordUserAccess.replaceOne(discordUser);
    }

    private boolean validateArgs(String[] args, MessageReceivedEvent event) {
        if (args.length < 3) {
            event.getChannel().sendMessage(USAGE).queue();
            return false;
        }
        return true;
    }

    private boolean validateUsernameNotExists(DiscordUser discordUser, MessageReceivedEvent event) {
        if (discordUser.getRunescapeUsername() != null) {
            event.getChannel().sendMessage(USERNAME_EXISTS).queue();
            return false;
        }
        return true;
    }

    private boolean validateUsernameNotInUse(String playerName, DiscordUserAccess discordUserAccess, MessageReceivedEvent event) {
        final DiscordUser existingRunescapeUser = discordUserAccess.findByRunescapeUsername(playerName);
        if (existingRunescapeUser != null) {
            event.getChannel().sendMessage(USERNAME_IN_USE).queue();
            return false;
        }
        return true;
    }
}

