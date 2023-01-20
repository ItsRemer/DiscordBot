package remibot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlinkoBot extends ListenerAdapter {

    private static JDA jda;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final Queue<Plinko> games = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) throws LoginException, InterruptedException {
        JDA bot = JDABuilder.createDefault("MTA2NTEwNTc0NjI4MDMxNzA1OA.G9pnTj.MwdFqmshpPmaIH3pM-2GYu248dXqfmXUSDVMTQ")
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.playing("Hosted by Remi"))
                .build();
        bot.addEventListener(new PlinkoBot());



        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            executorService.submit(new PlinkoWorker());
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getMessage().getContentRaw().startsWith("!plinko")) {
            return;
        }
        Plinko game = createPlinkoGame(event);
        games.offer(game);
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