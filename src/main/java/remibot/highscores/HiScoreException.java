package remibot.highscores;

public class HiScoreException extends RuntimeException {
    public HiScoreException(final Exception e) {
        super(e);
    }
}
