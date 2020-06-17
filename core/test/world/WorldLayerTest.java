package world;

import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.world.WorldLayer;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class WorldLayerTest {

    private static WorldLayer layer;

    @BeforeAll
    static void initAll() {
        Log.instance = new Log();
        Log.instance.init();
    }

    @BeforeEach
    private void initEach() {
        layer = new WorldLayer(10, 10);
        for(int i = 0; i < 10; i++) for(int j = 0; j < 10; j++) {
            layer.blockLayer[i][j][0] = (byte) (i + j);
            layer.blockLayer[i][j][1] = (byte) (i + j);
        }
    }

    @Test
    public void BlockTest() {
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                assertEquals(i + j, layer.getBlock(i, j));

        for(int i = -10; i < 0; i++)
            for(int j = 0; j < 10; j++)
                assertEquals(i + j + 10, layer.getBlock(i, j));
        Log.test("Block test - PASSED");
    }

    @Test
    public void StateTest() {
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                assertEquals(i + j, layer.getState(i, j));

        for(int i = -10; i < 0; i++)
            for(int j = 0; j < 10; j++)
                assertEquals(i + j + 10, layer.getState(i, j));
        Log.test("State test - PASSED");
    }

    @Test
    public void SetterStateTest() {
        layer.setState(5, 0, (byte) 10);

        assertEquals(10, layer.getState(5, 0));
        Log.test("Set state test - PASSED");
    }

    @Test
    public void SetterStateInfinityTest() {
        layer.setState(-5, 0, (byte) 10);
        layer.setState(10, 1, (byte) 10);

        assertEquals(10, layer.getState(5, 0));
        assertEquals(10, layer.getState(0, 1));
        Log.test("Set state infinity X - PASSED");
    }

    @Test
    public void SetterBlockTest() {
        layer.setBlock(5, 0, (byte) 10);

        assertEquals(10, layer.getBlock(5, 0));
        Log.test("Set block test - PASSED");
    }

    @Test
    public void SetterBlockInfinityTest() {
        layer.setBlock(-5, 0, (byte) 10);
        layer.setBlock(10, 1, (byte) 10);

        assertEquals(10, layer.getBlock(5, 0));
        assertEquals(10, layer.getBlock(0, 1));
        Log.test("Set block infinity X - PASSED");
    }

    @AfterAll
    static void afterAll() {
        Log.instance.save();
    }

}
