import de.undefinedhuman.sandboxgame.editor.BlockTextureGenerator;
import de.undefinedhuman.sandboxgame.engine.file.FileUtils;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BlockTextureTest {

    static BufferedImage baseTexture, expectedIconTexture, expectedBlockTexture;
    static String name = "Dirt";
    static Color borderColor = new Color(71, 35, 0);

    @BeforeAll
    static void initAll() throws IOException {
        Log.instance = new Log();
        Log.instance.init();

        baseTexture = ImageIO.read(new FsFile("../assets/test/" + name + ".png", false).getFile());
        expectedIconTexture = ImageIO.read(new FsFile("../assets/test/" + name + "-Icon.png", false).getFile());
        expectedBlockTexture = ImageIO.read(new FsFile("../assets/test/" + name + "-Texture.png", false).getFile());
    }

    @Test
    public void ExpectedImagesTest() {
        assertNotNull(baseTexture);
        Log.test("Base texture - PASSED");
        assertNotNull(expectedIconTexture);
        assertEquals(16, expectedIconTexture.getWidth());
        assertEquals(16, expectedIconTexture.getHeight());
        Log.test("Expected Icon texture - PASSED");
        assertNotNull(expectedBlockTexture);
        assertEquals(288, expectedBlockTexture.getWidth());
        assertEquals(18, expectedBlockTexture.getHeight());
        Log.test("Expected Block texture - PASSED");
    }

    @Test
    public void IconTextureGenerationTest() {
        BufferedImage actualIconTexture = BlockTextureGenerator.generateIcon("../assets/test/Block-Icon-Template.png", baseTexture, borderColor);
        assertNotNull(actualIconTexture);
        assertEquals(16, actualIconTexture.getWidth());
        assertEquals(16, actualIconTexture.getHeight());
        assertTrue(checkEachPixelOfTestImage(expectedIconTexture, actualIconTexture));
        Log.test("Block Icon Texture - PASSED");
    }

    @Test
    public void BlockTextureGenerationTest() {
        BlockTextureGenerator.generateBlockTexture("../assets/test/Block-Template.png", baseTexture, "../assets/test/", borderColor);
        BufferedImage actualBlockTexture = null;
        try { actualBlockTexture = ImageIO.read(new FsFile("../assets/test/items/" + name + "/Texture.png", false).getFile()); } catch (IOException e) { e.printStackTrace(); }
        assertNotNull(actualBlockTexture);
        assertEquals(288, actualBlockTexture.getWidth());
        assertEquals(18, actualBlockTexture.getHeight());
        assertTrue(checkEachPixelOfTestImage(expectedBlockTexture, actualBlockTexture));
        Log.test("Block Texture - PASSED");
    }

    @AfterAll
    static void afterAll() {
        FsFile templateDir = new FsFile("../assets/test/Dirt/", true);
        FileUtils.deleteFile(templateDir.getFile());
        FsFile itemsDir = new FsFile("../assets/test/items/", true);
        FileUtils.deleteFile(itemsDir.getFile());
        Log.instance.save();
    }

    private boolean checkEachPixelOfTestImage(BufferedImage expectedTexture, BufferedImage actualTexture) {
        for (int x = 0; x < expectedTexture.getWidth(); x++)
            for(int y = 0; y < expectedTexture.getHeight(); y++)
                if(expectedTexture.getRGB(x, y) != actualTexture.getRGB(x, y)) return false;
        return true;
    }

}
