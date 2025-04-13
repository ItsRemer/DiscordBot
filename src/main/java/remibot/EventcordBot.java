package remibot;

import com.google.common.collect.ImmutableMap;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import remibot.command.Command;
import remibot.command.impl.*;
import remibot.mongodb.MongoDBConnection;
import remibot.plinko.PlinkoWorker;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventcordBot extends ListenerAdapter {

    private static JDA jda;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static final MongoDBConnection mongoDB;

    private final Map<String, Command> COMMANDS = ImmutableMap.<String, Command>builder()
            .put("!task", new AssignTaskCommand())
            .put("!canceltask", new CancelTaskCommand())
            .put("!checktask", new CheckTaskCommand())
            .put("!clear", new ClearCommand())
            .put("!setname", new SetUsernameCommand())
            .put("!kc", new KillCountCommand())
            .put("!showname", new ShowNameCommand())
            .put("!plinko", new PlinkoCommand())
            .put("!points", new ShowPointsCommand())
            .put("!skc", new SearchKillCountCommand())
            .put("!commands", new HelpCommand())
            .put("!buyin", new SignUpCommand())
            .put("!paid", new SignUpCommand())
            .put("!set_bingo_items", new BingoUpdateCommand())
            .put("!clear_bingo_items", new BingoClearCommand())

            .build();

    static {
        System.out.println("Connecting to mongodb...");
        mongoDB = new MongoDBConnection("#####");
        System.out.println("Mongodb connection established!");
    }

    public static void main(String[] args) {
        JDA bot = JDABuilder.createDefault("#####")
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
        String[] args = event.getMessage().getContentRaw().split(" ");
        Command command = COMMANDS.get(args[0]);

        if (command == null) {
            return;
        }

        try {
            command.handle(args, event);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
