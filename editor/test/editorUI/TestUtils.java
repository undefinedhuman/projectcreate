package editorUI;

import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestUtils {

    // Threshold 0 -> 0% to 1 -> 100%, threshold included
    public static String compareScreenshots(BufferedImage actualImage, String textureName) {
        BufferedImage expectedImage = loadExpectedImage(textureName);
        if(expectedImage == null) return textureName + " - Expected image is null! - FAILED";
        if(actualImage == null) return textureName + " - Actual image is null! - FAILED";
        if(expectedImage.getWidth() != actualImage.getWidth()) return textureName + " - Width of actual and expected image don't match! - FAILED";
        if(expectedImage.getHeight() != actualImage.getHeight()) return textureName + " - Height of actual and expected image don't match! - FAILED";
        float difference = checkPixelOfImage(expectedImage, actualImage);
        float percent = (int) ((difference * 100) * 100) / 100f;
        if (difference <= 0.000125f) return textureName + " - Match - 0% difference! - PASSED";
        else if (difference <= Variables.E2E_THRESHOLD) {
            try {
                ImageIO.write(actualImage, "png", new FsFile("test/editorUI/" + textureName + "/PASSED_" + textureName + "_" + percent + "%.png", false).getFile());
                ImageIO.write(expectedImage, "png", new FsFile("test/editorUI/" + textureName + "/" + textureName + "_Expected.png", false).getFile());
            } catch (IOException e) {
                return textureName + " - Can't save passed images - " + percent + "% difference! - ERROR! - PASSED";
            }
            return textureName + " - Match - " + percent + "% difference - PASSED";
        } else {
            try {
                ImageIO.write(actualImage, "png", new FsFile("test/editorUI/" + textureName + "/FAILED_" + textureName + "_" + percent + "%.png", false).getFile());
                ImageIO.write(expectedImage, "png", new FsFile("test/editorUI/" + textureName + "/" + textureName + "_Expected.png", false).getFile());
            } catch (IOException e) {
                return textureName + " - Can't save failed images - " + percent + "% difference! - ERROR! - FAILED";
            }
            return textureName + " - Don't match - " + percent + "% difference - FAILED";
        }
    }

    public static BufferedImage loadExpectedImage(String name) {
        BufferedImage image = null;
        try { image = ImageIO.read(new File("test/editorUI/" + name + "_Expected.png")); } catch (IOException e) { e.printStackTrace(); }
        return image;
    }

    public static BufferedImage screenshot(JFrame frame) {
        BufferedImage img = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
        frame.paint(img.getGraphics());
        return img;
    }

    public static float checkPixelOfImage(BufferedImage expectedTexture, BufferedImage actualTexture) {
        float actualPercent = 0;
        for (int x = 0; x < expectedTexture.getWidth(); x++)
            for(int y = 0; y < expectedTexture.getHeight(); y++)
                if(expectedTexture.getRGB(x, y) != actualTexture.getRGB(x, y)) actualPercent++;
        return actualPercent / (expectedTexture.getWidth() * expectedTexture.getHeight());
    }

    public static void sleep() {
        sleep(Variables.E2E_SLEEP_AMOUNT);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
