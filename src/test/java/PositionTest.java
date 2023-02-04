import org.testng.Assert;
import org.testng.annotations.Test;
import remibot.plinko.Position;

public class PositionTest {

    @Test
    public void testClamping() {
        final Position position = new Position(3, 3);

        final Position moved = position.transform(2, 0);
        Assert.assertEquals(moved.x(), 5);
        Assert.assertEquals(moved.y(), 3);

        final Position clamped = moved.clamp(-1, 2);
        Assert.assertEquals(clamped.x(), 0);
        Assert.assertEquals(clamped.y(), 2);
    }
}
