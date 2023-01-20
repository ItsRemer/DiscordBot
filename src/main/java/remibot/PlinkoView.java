package remibot;

public final class PlinkoView {

    private final Plinko plinko;

    public PlinkoView(Plinko plinko) {
        this.plinko = plinko;
    }

    public String render() {
        final Position ballPosition = plinko.getBallPosition();
        final StringBuilder sb = new StringBuilder();
        sb.append("Playing: \n");
        for (int x = 0; x < Plinko.WIDTH; x++) {
            for (int y = 0; y < Plinko.HEIGHT; y++) {
                if (x == ballPosition.x() && y == ballPosition.y()) {
                    sb.append("*");
                } else {
                    sb.append("0");
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
