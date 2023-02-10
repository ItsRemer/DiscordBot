package remibot.command.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import remibot.EventcordBot;
import remibot.command.Command;
import remibot.highscores.HiScoreScraper;
import remibot.highscores.KillCountEntry;
import remibot.mongodb.DiscordUser;
import remibot.mongodb.DiscordUserAccess;

import java.util.Arrays;
import java.util.Map;

public final class KillCountCommand implements Command {

    public final HiScoreScraper hiScoreScraper = new HiScoreScraper();

    @Override
    public void handle(String[] args, MessageReceivedEvent event) {
        final DiscordUserAccess discordUserAccess = EventcordBot.mongoDB.getDiscordUserAccess();

        final String playerName;
        final String accountTypeParameter;
        final DiscordUser discordUser = discordUserAccess.findByDiscordId(event.getAuthor().getId());
        if (discordUser == null || discordUser.getRunescapeUsername() == null) {
            event.getChannel().sendMessage("Please make sure you have set your username by doing !setname <username> <accountType>").queue();
            return;
        }
        playerName = discordUser.getRunescapeUsername();
        accountTypeParameter = discordUser.getAccountType();

        final String bossName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        final Map<String, KillCountEntry> killCounts = hiScoreScraper.scrapeKillCounts(HiScoreScraper.AccountType.valueOf(accountTypeParameter), playerName);
        final KillCountEntry entry = killCounts.get(bossName.toLowerCase());
        if (entry != null) {
            event.getChannel().sendMessage("Your account " + playerName + " has " + entry.score() + " kills for the boss " + bossName + ".").queue();
        } else {
            event.getChannel().sendMessage("Unable to find any results for " + playerName + " and boss type: " + bossName).queue();
        }
    }
}

