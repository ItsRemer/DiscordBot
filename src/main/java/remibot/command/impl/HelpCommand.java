package remibot.command.impl;


import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import remibot.command.Command;
import java.util.HashMap;
import java.util.Map;

public final class HelpCommand implements Command {

    private static final Map<String, String> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put("!skc <username> <N,IM,UIM,HCIM> <boss name>", "Search the inputted username's kc.");
        COMMANDS.put("!setname <username> <N,IM,UIM,HCIM>", "Linking your OSRS username, needed for other commands.");
        COMMANDS.put("!showname", "Display your current linked OSRS username.");
        COMMANDS.put("!task", "Get a slayer task.");
        COMMANDS.put("!checktask", "Check your current slayer task.");
        COMMANDS.put("!canceltask", "(Temporary) Cancel your current slayer task.");
        COMMANDS.put("!points", "Check your current slayer point amount.");
        COMMANDS.put("!plinko", "Run a plinko game, !plinko @discordname is available as well.");
    }

    @Override
    public void handle(String[] args, MessageReceivedEvent event) {
        StringBuilder commands = new StringBuilder("Command List:\n");
        for (Map.Entry<String, String> entry : COMMANDS.entrySet()) {
            commands.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        event.getChannel().sendMessage(commands.toString()).queue();
    }
}