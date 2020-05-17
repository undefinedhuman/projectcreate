package file;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LineIOTest {

    @BeforeAll
    static void initAll() {
        Log.instance = new Log();
        Log.instance.init();
    }

    @Test
    public void LineIO() {
        LineWriter writer = new LineWriter(false);
        writeData(writer);

        assertEquals("String;1;1.0;1;1.0;2.0;1.0;2.0;3.0;", writer.getData());

        LineSplitter splitter = new LineSplitter(writer.getData(), false);
        assertEquals("String", splitter.getNextString());
        assertEquals(1, splitter.getNextInt());
        assertEquals(1.0f, splitter.getNextFloat());
        assertTrue(splitter.getNextBoolean());
        assertEquals(new Vector2(1, 2), splitter.getNextVector2());
        assertEquals(new Vector3(1, 2, 3), splitter.getNextVector3());

        Log.test("Line IO - PASSED");
    }

    @Test
    public void LineIOBase64() {
        LineWriter writer = new LineWriter(true);
        writeData(writer);

        assertEquals("U3RyaW5n;MQ==;MS4w;MQ==;MS4w;Mi4w;MS4w;Mi4w;My4w;", writer.getData());

        LineSplitter splitter = new LineSplitter(writer.getData(), true);
        assertEquals("String", splitter.getNextString());
        assertEquals(1, splitter.getNextInt());
        assertEquals(1.0f, splitter.getNextFloat());
        assertTrue(splitter.getNextBoolean());
        assertEquals(new Vector2(1, 2), splitter.getNextVector2());
        assertEquals(new Vector3(1, 2, 3), splitter.getNextVector3());

        Log.test("Line IO Base64 - PASSED");
    }

    @AfterAll
    static void afterAll() {
        Log.instance.save();
    }

    private void writeData(LineWriter writer) {
        writer.writeString("String");
        writer.writeInt(1);
        writer.writeFloat(1.0f);
        writer.writeBoolean(true);
        writer.writeVector2(new Vector2(1, 2));
        writer.writeVector3(new Vector3(1, 2, 3));
    }

}
