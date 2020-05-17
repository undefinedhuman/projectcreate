package file;

import de.undefinedhuman.sandboxgame.engine.file.FileUtils;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileUtilsTest {

    @BeforeAll
    static void initAll() {
        Log.instance = new Log();
        Log.instance.init();
    }

    @Test
    public void ReadBooleanFromString() {
        assertTrue(FileUtils.readBoolean("true"));
        assertFalse(FileUtils.readBoolean("false"));
        Log.test("Read Bool from String - PASSED");
    }

    @Test
    public void ReadBooleanFromInt() {
        assertTrue(FileUtils.readBoolean("1"));
        assertFalse(FileUtils.readBoolean("0"));
        assertFalse(FileUtils.readBoolean("2"));
        Log.test("Read Bool from Int - PASSED");
    }

    @Test
    public void DontReadBooleanFromString() {
        assertFalse(FileUtils.readBoolean("fals"));
        assertFalse(FileUtils.readBoolean("tru"));
        Log.test("Don't read Bool from String - PASSED");
    }

    @Test
    public void DeleteFile() {
        File file = new FsFile("./test.file", false).getFile();

        assertTrue(file.exists());
        FileUtils.deleteFile(file);

        assertFalse(file.exists());
        Log.test("Delete File - PASSED");
    }

    @Test
    public void DeleteDir() {
        File dir = new FsFile("./testDir/", true).getFile();

        assertTrue(dir.exists());
        FileUtils.deleteFile(dir);

        assertFalse(dir.exists());
        Log.test("Delete Dir - PASSED");
    }

    @Test
    public void DeleteDirWithFile() {
        File dir = new FsFile("./testDir/", true).getFile();
        File file = new FsFile("./testDir/test.file", false).getFile();

        assertTrue(dir.exists());
        assertTrue(file.exists());
        FileUtils.deleteFile(dir);

        assertFalse(file.exists());
        assertFalse(dir.exists());
        Log.test("Delete Dir with file - PASSED");
    }

    @AfterAll
    static void afterAll() {
        Log.instance.save();
    }

}
