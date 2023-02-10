package remibot.command.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import remibot.EventcordBot;
import remibot.command.Command;
import remibot.highscores.HiScoreScraper;
import remibot.highscores.KillCountEntry;
import remibot.mongodb.DiscordUser;
import remibot.mongodb.DiscordUserAccess;

import java.util.Map;

public final class CheckTaskCommand implements Command {

    public final HiScoreScraper hiScoreScraper = new HiScoreScraper();

    @Override
    public void handle(String[] args, MessageReceivedEvent event) {
        final DiscordUserAccess discordUserAccess = EventcordBot.mongoDB.getDiscordUserAccess();
        final DiscordUser discordUser = discordUserAccess.findByDiscordId(event.getAuthor().getId());
        if (discordUser == null || discordUser.getRunescapeUsername() == null) {
            event.getChannel().sendMessage("Please make sure you have set your username by doing !setname <username> <accountType>").queue();
            return;
        }
        if (discordUser.getSlayerTask() == null) {
            event.getChannel().sendMessage("You do not have an active slayer task.").queue();
            return;
        }
        final Map<String, KillCountEntry> killCounts = hiScoreScraper.scrapeKillCounts(HiScoreScraper.AccountType.valueOf(discordUser.getAccountType()), discordUser.getRunescapeUsername());
        final KillCountEntry entry = killCounts.get(discordUser.getSlayerTask().getTaskName().toLowerCase());

        if (entry == null) {
            event.getChannel().sendMessage("Could not find Kill Count for your Old School runescape account.").queue();
            return;
        }
        int currentKC = entry.score();
        int taskAmount = discordUser.getSlayerTask().getAmount();
        int taskKC = discordUser.getSlayerTask().getCurrentKC() + taskAmount;
        int slayerPoints = discordUser.getSlayerPoints();

        if (currentKC >= taskKC) {
            event.getChannel().sendMessage("Task complete! You have killed " + taskAmount + " " + discordUser.getSlayerTask().getTaskName() + ", and have been rewarded 1 slayer point.").queue();
            discordUser.setSlayerTask(null);
            discordUserAccess.replaceOne(discordUser);
            discordUser.setSlayerPoints(slayerPoints + 1);
        } else {
            event.getChannel().sendMessage("You have killed " + (currentKC - discordUser.getSlayerTask().getCurrentKC()) + " out of " + taskAmount + " " + discordUser.getSlayerTask().getTaskName() + ".").queue();
        }
    }
}
