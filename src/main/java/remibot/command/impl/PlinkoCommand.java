package remibot.command.impl;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import remibot.command.Command;
import remibot.plinko.Plinko;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class PlinkoCommand implements Command {

    private static final Queue<Plinko> games = new ConcurrentLinkedQueue<>();

    @Override
    public void handle(String[] args, MessageReceivedEvent event) {
        Member member = event.getMember();
        if (member == null) {
            return;
        }
        List<Role> roles = member.getRoles();
        for (Role role : roles) {
            if (role.getName().equals("Coordinators") || role.getName().equals("Plinko Captain") || role.getName().equals("Admin") || role.getName().equals("Jefe")) {
                Plinko game = createPlinkoGame(event);
                games.offer(game);
                return;
            }
        }
        event.getMessage().reply("You do not have permissions to use this command.").queue();
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
