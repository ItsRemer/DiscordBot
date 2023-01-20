package remibot;

public record Position(int x, int y) {

    public Position clamp(int maxX, int maxY) {
        int clampedX = Math.max(0, Math.min(x, maxX - 1));
        int clampedY = Math.max(0, Math.min(y, maxY - 1));
        return new Position(clampedX, clampedY);
    }

    public Position transform(int deltaX, int deltaY) {
        return new Position(x + deltaX, y + deltaY);
    }
}
