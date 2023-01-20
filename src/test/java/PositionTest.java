import remibot.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PositionTest {

    @Test
    public void testClamping() {
        final Position position = new Position(3, 3);

        final Position moved = position.transform(2, 0);
        Assertions.assertEquals(moved.x(), 5);
        Assertions.assertEquals(moved.y(), 3);

        final Position clamped = moved.clamp(-1, 2);
        Assertions.assertEquals(clamped.x(), 0);
        Assertions.assertEquals(clamped.y(), 2);
    }
}
