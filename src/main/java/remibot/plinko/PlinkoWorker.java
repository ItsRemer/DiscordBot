package remibot.plinko;

import net.dv8tion.jda.api.entities.Role;
import remibot.EventcordBot;
import remibot.command.impl.PlinkoCommand;

import java.util.Random;

public class PlinkoWorker implements Runnable {

    private static final int[] scores = new int[]{1, 2, 3, 4, 5, 6, 7};

    private static final Random random = new Random();

    @Override
    public void run() {
            while (true) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            Plinko game = PlinkoCommand.getGames().poll();
            if (game == null) {
                continue;
            }
            final Plinko fGame = game;
            game.getChannel().sendMessage("Plinko game starting for " + (game.getRole().isEmpty() ? game.getPlayerName() : game.getRole())).queue(m -> {
                m.editMessage(new PlinkoView(fGame).render()).queue(); // send initial board state

                Plinko currentGame = fGame;

                for (int y = 0; y < Plinko.HEIGHT - 1; y++) {
                    final int deltaY = random.nextInt(2) == 0 ? -1 : 1;
                    currentGame = currentGame.transform(1, deltaY);
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (y == Plinko.HEIGHT - 2) { // The loop is in the last iteration, and the ball is in it's final position
                        final Plinko finalState = currentGame;
                        m.editMessage(new PlinkoView(currentGame).render()).queue(lastMessage -> { // Wait until we send the final position, and then send
                            // a new message containing the result of the player's game.
                            final int score = scores[finalState.getBallPosition().y()];
                            if (!game.getRole().isEmpty()) {
                                StringBuilder roleNames = new StringBuilder();
                                for (Role role : game.getRole()) {
                                    roleNames.append(role.getAsMention()).append(", ");
                                }
                                finalState.getChannel().sendMessage(roleNames + " has landed on: " + score).queue();
                            } else {
                                finalState.getChannel().sendMessage(finalState.getPlayerName() + " has landed on: " + score).queue();
                            }
                        });
                    } else {
                        m.editMessage(new PlinkoView(currentGame).render()).queue();
                    }
                }
            });
        }
    }
}