package de.undefinedhuman.sandboxgame.editor;

import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.log.Log;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BlockTextureGenerator {

    private static String name = "Dirt";
    private static String texturePath = "./assets/editor/items/" + name + ".png";
    private static Color borderColor = new Color(71, 35, 0);
    private static int templateCount = 16;
    private static int blockWidth = 16;

    public static void main(String[] args) throws IOException {

        Log.instance = new Log();
        FsFile baseTextureFile = new FsFile(texturePath, false);
        BufferedImage baseTexture = ImageIO.read(baseTextureFile.getFile());
        BufferedImage image = new BufferedImage(templateCount * blockWidth, blockWidth, BufferedImage.TYPE_INT_ARGB);
        BufferedImage icon = new BufferedImage(blockWidth, blockWidth, BufferedImage.TYPE_INT_ARGB);
        BufferedImage templateTexture = ImageIO.read(new FsFile("./assets/editor/template/Block-Template.png", false).getFile());
        for(int x = 0; x < blockWidth * templateCount; x++)
            for(int y = 0; y < blockWidth; y++) {
                int color = templateTexture.getRGB(x, y);
                if(color == Color.RED.getRGB()) continue;
                if(color == Color.GREEN.getRGB()) image.setRGB(x, y, borderColor.getRGB());
                if(color == Color.BLUE.getRGB()) image.setRGB(x, y, baseTexture.getRGB(x%blockWidth, y));
            }

        BufferedImage iconTemplateTexture = ImageIO.read(new FsFile("./assets/editor/template/Sprite-Template-1.png", false).getFile());
        for(int x = 0; x < blockWidth; x++)
            for(int y = 0; y < blockWidth; y++) {
                int color = iconTemplateTexture.getRGB(x, y);
                if(color == Color.RED.getRGB()) continue;
                if(color == Color.GREEN.getRGB()) icon.setRGB(x, y, borderColor.getRGB());
                if(color == Color.BLUE.getRGB()) icon.setRGB(x, y, baseTexture.getRGB(x, y));
            }

        ImageIO.write(icon, "png", new FsFile("./assets/editor/" + name + "-Icon.png", false).getFile());
        ImageIO.write(image, "png", new FsFile("./assets/editor/" + name + ".png", false).getFile());

    }

    private static BufferedImage rotateImage(BufferedImage sourceImage) {

        int width = sourceImage.getWidth(), height = sourceImage.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, sourceImage.getType());
        for(int y = 0; y < width; y++) for(int x = 0; x < height; x++) newImage.setRGB(height-y-1, x, sourceImage.getRGB(x, y));
        return newImage;

    }

}
