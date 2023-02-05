package remibot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import remibot.highscores.HiScoreException;
import remibot.highscores.HiScoreScraper;
import remibot.highscores.KillCountEntry;
import remibot.plinko.Plinko;
import remibot.plinko.PlinkoWorker;
import remibot.slayer.SlayerBosses;
import remibot.slayer.SlayerBossesDatabase;
import remibot.slayer.SlayerBossesPlayerInformation;

import javax.security.auth.login.LoginException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EventcordBot extends ListenerAdapter {

    private static JDA jda;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final Queue<Plinko> games = new ConcurrentLinkedQueue<>();

    private static final SlayerBossesDatabase database = SlayerBossesDatabase.getInstance();

    private final HiScoreScraper hiScoreScraper = new HiScoreScraper();

    public static void main(String[] args) throws LoginException, InterruptedException {
        JDA bot = JDABuilder.createDefault("MTA2NTEwNTc0NjI4MDMxNzA1OA.G9pnTj.MwdFqmshpPmaIH3pM-2GYu248dXqfmXUSDVMTQ")
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.playing("Developed by Remi"))
                .build();
        bot.addEventListener(new EventcordBot());


        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            executorService.submit(new PlinkoWorker());
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try {
            if (event.getMessage().getContentRaw().startsWith("!clear")) {
                handleClearCommand(event);
            } else if (event.getMessage().getContentRaw().startsWith("!plinko")) {
                handlePlinkoCommand(event);
            } else if (event.getMessage().getContentRaw().startsWith("!slay")) {
                handleSlayCommand(event);
            } else if (event.getMessage().getContentRaw().startsWith("!info")) {
                handleSlayInfoCommand(event);
            } else if (event.getMessage().getContentRaw().startsWith("!kc")) {
                String[] args = event.getMessage().getContentRaw().split(" ");
                handleKillCountCommand(event, args);
            } else if (event.getMessage().getContentRaw().startsWith("!setname")) {
                SlayerBossesPlayerInformation.setPlayerName(event.getMessage().getAuthor(), "Its RemiQ");
                event.getMessage().reply("Your username has been set to Its RemiQ").queue();
            }
        } catch (
                final RuntimeException e) {
            e.printStackTrace();
        }

    }

    private boolean handlePlinkoCommand(MessageReceivedEvent event) {
        Member member = event.getMember();
        if (member == null) {
            return true;
        }
        List<Role> roles = member.getRoles();
        for (Role role : roles) {
            if (role.getName().equals("Coordinators") || role.getName().equals("Plinko Captain") || role.getName().equals("Admin")) {
                Plinko game = createPlinkoGame(event);
                games.offer(game);
                return true;
            }
        }
        event.getMessage().reply("You do not have permissions to use this command.").queue();
        return false;
    }

    private void handleKillCountCommand(MessageReceivedEvent event, String[] args) throws HiScoreException {
        if (args.length != 4) {
            event.getChannel().sendMessage("Wrong format. Please type !KC (username) (accountType) (bossName)").queue();
            return;
        }

        final String playerName = args[1];
        final String accountTypeParameter = args[2];
        final String bossName = event.getMessage().getContentRaw().substring(event.getMessage().getContentRaw().indexOf(args[3]));

        final Map<String, KillCountEntry> killCounts = hiScoreScraper.scrapeKillCounts(HiScoreScraper.AccountType.valueOf(accountTypeParameter), playerName);

        final KillCountEntry entry = killCounts.get(bossName.toLowerCase());
        if (entry != null) {
            event.getChannel().sendMessage("The player " + playerName + " has " + entry.score() + " kills for the boss " + bossName).queue();
        } else {
            event.getChannel().sendMessage("Unable to find any results for " + playerName + " and boss type: " + bossName).queue();
        }
    }

    private void handleClearCommand(MessageReceivedEvent event) {
        event.getMessage().getChannel().getIterableHistory().forEach(m -> {
            m.delete().queue();
        });
    }

    private void handleSlayCommand(MessageReceivedEvent event) {
        ArrayList<SlayerBosses> bosses = database.getBosses();
        if (bosses.size() == 0) {
            event.getChannel().sendMessage("There are no bosses in the database").queue();
            return;
        }
        // get random boss from list
        SlayerBosses randomBoss = bosses.get((int) (Math.random() * bosses.size()));
        // generate random number of kills
        int kills = (int) (Math.random() * 100) + 1;
        event.getChannel().sendMessage("You have been given a task of " + kills + "x " + randomBoss.getName() + " to kill.").queue();
    }

    private void handleSlayInfoCommand(MessageReceivedEvent event) {
        String[] command = event.getMessage().getContentRaw().split(" ");
        if (command.length != 2) {
            event.getChannel().sendMessage("Invalid command format. Please use !info <boss name>").queue();
            return;
        }
        String bossName = command[1];
        SlayerBosses boss = database.getBoss(bossName);
        if (boss == null) {
            event.getChannel().sendMessage("Boss not found").queue();
            return;
        }
        event.getChannel().sendMessage("Name: " + boss.getName() + "\nWeakness: " + boss.getWeakness() + "\nSlayer level requirement: " + boss.getSlayerLevel()).queue();
    }


    private Plinko createPlinkoGame(MessageReceivedEvent event) {
        final String playerId = event.getAuthor().getId();
        final String playerName = event.getAuthor().getName();
        List<Role> roles = event.getMessage().getMentions().getRoles();
        List<Role> matchingRoles = new ArrayList<>();
        if (!roles.isEmpty()) {
            for (Role role : event.getGuild().getRoles()) {
                if (roles.get(0).getName().equalsIgnoreCase(role.getName())) {
                    matchingRoles.add(role);
                    break;
                }
            }
        }
        if (!matchingRoles.isEmpty() || event.getMessage().getMentions().getRoles().size() > 0) {
            return new Plinko(event.getChannel(), matchingRoles);
        } else {
            return new Plinko(event.getChannel(), playerId, playerName);
        }
    }

    public static Queue<Plinko> getGames() {
        return games;
    }
}