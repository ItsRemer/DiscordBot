package remibot;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import java.util.ArrayList;
import java.util.List;

public class Plinko {

    private static final Position DEFAULT_BALL_POSITION = new Position(0, 3);

    public static final int WIDTH = 7;

    public static final int HEIGHT = 7;

    private final MessageChannelUnion channel;

    private final String playerId;

    private final Position ballPosition;

    private final String playerName;

    private List<Role> roles = new ArrayList<>();

    public Plinko(MessageChannelUnion channel, final String playerId, final String playerName) {
        this(channel, playerId, playerName, DEFAULT_BALL_POSITION);
    }

    public Plinko(MessageChannelUnion channel, String playerId, String playerName, final Position ballPosition) {
        this.channel = channel;
        this.playerId = playerId;
        this.playerName = playerName;
        this.ballPosition = ballPosition;
    }

    public Plinko(MessageChannelUnion channel, final List<Role> roles) {
        this(channel, null, null, DEFAULT_BALL_POSITION);
        this.roles = roles;
    }

    public Plinko transform(int deltaX, int deltaY) {
        return new Plinko(channel, playerId, playerName, ballPosition.transform(deltaX, deltaY).clamp(WIDTH, HEIGHT));
    }

    public MessageChannelUnion getChannel() {
        return channel;
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<Role> getRole() {
        return roles;
    }

    public Position getBallPosition() {
        return ballPosition;
    }
}
