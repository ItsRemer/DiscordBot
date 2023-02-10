package remibot.command.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import remibot.EventcordBot;
import remibot.command.Command;
import remibot.highscores.HiScoreScraper;
import remibot.highscores.KillCountEntry;
import remibot.mongodb.DiscordUser;
import remibot.mongodb.DiscordUserAccess;
import remibot.mongodb.SlayerTask;
import remibot.slayer.Bosses;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class AssignTaskCommand implements Command {

    public final HiScoreScraper hiScoreScraper = new HiScoreScraper();

    public static final Random random = new Random();

    @Override
    public void handle(String[] args, MessageReceivedEvent event) {
        final DiscordUserAccess discordUserAccess = EventcordBot.mongoDB.getDiscordUserAccess();
        final DiscordUser discordUser = discordUserAccess.findByDiscordId(event.getAuthor().getId());

        if (discordUser == null || discordUser.getRunescapeUsername() == null) {
            event.getChannel().sendMessage("Please make sure you have set your username by doing !setname <username> <accountType>").queue();
            return;
        }

        final Map<String, KillCountEntry> killCounts = hiScoreScraper.scrapeKillCounts(HiScoreScraper.AccountType.valueOf(discordUser.getAccountType()), discordUser.getRunescapeUsername());

        final List<Map.Entry<String, KillCountEntry>> possibleTasks = killCounts.entrySet().stream().toList();

        if (possibleTasks.isEmpty()) {
            event.getChannel().sendMessage("You are not eligible to slay yet. Make sure you're on the OSRS hiscores with at least one boss!").queue();
            return;
        }

        if (discordUser.getSlayerTask() != null) {
            event.getChannel().sendMessage("You already have a slayer task, please complete it before trying again.").queue();
            return;
        }

        final List<Bosses> validTasks = Arrays.stream(Bosses.values())
                .filter(boss -> possibleTasks.stream().anyMatch(entry -> boss.getName().equalsIgnoreCase(entry.getKey())))
                .toList();
        if (validTasks.isEmpty()) {
            event.getChannel().sendMessage("You don't have any bosses on the OSRS hiscores that match with the possible tasks. Please check the hiscores and try again later.").queue();
            return;
        }

        final Bosses data = validTasks.get(random.nextInt(validTasks.size()));
        int minimum = data.getMinAmount();
        int maximum = data.getMaxAmount();
        int amount = minimum + random.nextInt(maximum - minimum + 1);

        final String taskName = data.getName();
        final Map.Entry<String, KillCountEntry> entry = possibleTasks.stream().filter(e -> e.getKey().equalsIgnoreCase(taskName)).findFirst().orElse(null);
        if (entry == null) {
            event.getChannel().sendMessage("Something went wrong when trying to assign a random boss. Message Remi if this continues to occur.").queue();
            return;
        }

        SlayerTask slayerTask = new SlayerTask();
        slayerTask.setAmount(amount);
        slayerTask.setCurrentKC(entry.getValue().score());
        slayerTask.setTaskName(data.getName());
        discordUser.setSlayerTask(slayerTask);
        discordUserAccess.replaceOne(discordUser);

        event.getChannel().sendMessage("You have been given a task of " + amount + "x " + data.getName() + " to kill.").queue();
    }
}
