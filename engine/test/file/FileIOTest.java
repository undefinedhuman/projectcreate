package file;

import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.FileUtils;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class FileIOTest {

    public static FsFile fileIO;

    public static String[] testLines = {
            "Test line 1",
            "Test line 1 part 2",
            "Test line 2",
            "Test line 2 part 2",
            "Test line 3",
            "Test line 3 part 2"
    };

    @BeforeAll
    static void initAll() {
        Log.instance = new Log();
        Log.instance.init();

        fileIO = new FsFile("./testIO.file", false);
    }

    @Test
    public void FileExists() {
        assertTrue(fileIO.exists());
        Log.test("File IO Exists - PASSED");
    }

    @Test
    public void FileWriting() {
        FileWriter writer = fileIO.getFileWriter(false);
        FileReader reader = fileIO.getFileReader(false);

        writeFile(writer);
        readFile(reader);

        Log.test("File IO - PASSED");
    }

    @Test
    public void FileWritingBase64() {
        FileWriter writer = fileIO.getFileWriter(true);
        FileReader reader = fileIO.getFileReader(true);

        writeFile(writer);
        readFile(reader);
        Log.test("File IO with Base64 - PASSED");
    }

    @AfterAll
    static void afterAll() {
        FileUtils.deleteFile(fileIO);
        Log.instance.save();
    }

    private boolean testStringArray(String[] expected, String[] actual) {
        if(expected.length != actual.length) return false;
        for(int i = 0; i < expected.length; i++)
            if(actual[i] == null || !expected[i].equals(actual[i])) return false;
        return true;
    }

    private void readFile(FileReader reader) {
        String[] readTestLines = new String[6];

        assertNotNull(reader);

        for(int i = 0; i < 3; i++) {
            String line = reader.nextLine();
            if(line == null || line.isEmpty()) continue;
            readTestLines[i*2] = reader.getNextString();
            readTestLines[i*2+1] = reader.getNextString();
        }

        try {
            assertTrue(testStringArray(testLines, readTestLines));
        } catch(Exception e) {
            Log.error("File reading failed: \n" + Arrays.toString(readTestLines));
        }
        reader.close();
    }

    private void writeFile(FileWriter writer) {
        assertNotNull(writer);

        for(int i = 0; i < 3; i++) {
            writer.writeString(testLines[i*2]);
            writer.writeString(testLines[i*2+1]);
            writer.nextLine();
        }
        writer.close();

        assertFalse(fileIO.isEmpty());
    }

}
