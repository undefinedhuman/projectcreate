package file;

import de.undefinedhuman.sandboxgame.engine.file.FileUtils;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileSystemTest {

    @BeforeAll
    static void initAll() {
        Log.instance = new Log();
        Log.instance.init();
    }

    @Test
    public void FileCreation() {
        File file = new FsFile("./test.file", false).getFile();
        assertTrue(file.exists());
        Log.test("Create File - PASSED");
        FileUtils.deleteFile(file);
    }

    @Test
    public void DirCreation() {
        File file = new FsFile("./testDir/", true).getFile();
        assertTrue(file.exists());
        Log.test("Create Dir - PASSED");
        FileUtils.deleteFile(file);
    }

    @Test
    public void CreateDirWithFile() {
        File file = new FsFile("./testDir/", true).getFile();
        assertTrue(file.exists());
        assertTrue(new FsFile("./testDir/test.file", false).getFile().exists());
        Log.test("Create Dir with file - PASSED");
        FileUtils.deleteFile(file);
    }

    @Test
    public void CreateDirWithFileAndDir() {
        File file = new FsFile("./testDir/", true).getFile();
        assertTrue(file.exists());
        assertTrue(new FsFile("./testDir/test.file", false).getFile().exists());
        assertTrue(new FsFile("./testDir/test2/", true).getFile().exists());
        assertTrue(new FsFile("./testDir/test2/test2.file", false).getFile().exists());
        Log.test("Create Dir with files and dir - PASSED");
        FileUtils.deleteFile(file);
    }

    @AfterAll
    static void afterAll() {
        Log.instance.save();
    }

}
