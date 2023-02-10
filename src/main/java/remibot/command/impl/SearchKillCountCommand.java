package remibot.command.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import remibot.command.Command;
import remibot.highscores.HiScoreScraper;
import remibot.highscores.KillCountEntry;
import java.util.Arrays;
import java.util.Map;

public final class SearchKillCountCommand implements Command {

    public final HiScoreScraper hiScoreScraper = new HiScoreScraper();

    @Override
    public void handle(String[] args, MessageReceivedEvent event) {

        if (args.length < 4) {
            event.getChannel().sendMessage("Wrong format. Please type !KC (username) (N,IM,UIM,HCIM) (bossName)").queue();
            return;
        }

        final String playerName = args[1];
        final String accountTypeParameter = args[2].toUpperCase();
        final String bossName = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
        final Map<String, KillCountEntry> killCounts = hiScoreScraper.scrapeKillCounts(HiScoreScraper.AccountType.valueOf(accountTypeParameter), playerName);


        final KillCountEntry entry = killCounts.get(bossName.toLowerCase());
        if (entry != null) {
            event.getChannel().sendMessage("The player " + playerName + " has " + entry.score() + " kills for the boss " + bossName).queue();
        } else {
            event.getChannel().sendMessage("Unable to find any results for " + playerName + " and boss type: " + bossName).queue();
        }
    }
}