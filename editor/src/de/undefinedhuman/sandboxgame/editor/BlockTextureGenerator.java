package de.undefinedhuman.sandboxgame.editor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import de.undefinedhuman.sandboxgame.engine.file.FileUtils;
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
        BufferedImage templateTexture = ImageIO.read(new FsFile("./assets/editor/template/Block-Template.png", false).getFile());
        for(int x = 0; x < templateCount; x++) {
            BufferedImage image = new BufferedImage(blockWidth, blockWidth, BufferedImage.TYPE_INT_ARGB);
            for(int z = 0; z < blockWidth; z++)
                for(int y = 0; y < blockWidth; y++) {
                    int tempX = x * blockWidth + z;
                    int color = templateTexture.getRGB(tempX, y);
                    if(color == Color.RED.getRGB()) continue;
                    if(color == Color.GREEN.getRGB()) image.setRGB(z, y, borderColor.getRGB());
                    if(color == Color.BLUE.getRGB()) image.setRGB(z, y, baseTexture.getRGB(z, y));
                }
            ImageIO.write(image, "png", new FsFile("./assets/editor/" + name + "/" + x + ".png", false).getFile());
        }

        generateIcon(baseTexture);

        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.duplicatePadding = true;
        settings.maxHeight = 32;
        settings.paddingX = 2;
        settings.paddingY = 2;
        settings.alphaThreshold = 0;
        settings.filterMin = Texture.TextureFilter.Nearest;
        settings.filterMag = Texture.TextureFilter.Nearest;
        settings.edgePadding = true;
        settings.stripWhitespaceX = true;
        settings.stripWhitespaceY = true;
        settings.bleed = true;
        settings.grid = false;
        settings.duplicatePadding = true;
        settings.pot = false;
        settings.alias = true;
        settings.ignoreBlankImages = false;
        settings.debug = false;
        settings.useIndexes = false;
        settings.premultiplyAlpha = false;
        settings.limitMemory = false;
        TexturePacker.process(settings, "./assets/editor/" + name, "./assets/editor/items/" + name + "/", "Texture");

        FsFile file = new FsFile("./assets/editor/items/" + name + "/Texture.atlas", false);
        FileUtils.deleteFile(file.getFile());

        FsFile tempDir = new FsFile("./assets/editor/" + name + "/", true);
        FileUtils.deleteFile(tempDir.getFile());

    }

    private static void generateIcon(BufferedImage baseTexture) throws IOException {
        BufferedImage icon = new BufferedImage(blockWidth, blockWidth, BufferedImage.TYPE_INT_ARGB);
        BufferedImage iconTemplateTexture = ImageIO.read(new FsFile("./assets/editor/template/Sprite-Template-1.png", false).getFile());
        for(int x = 0; x < blockWidth; x++)
            for(int y = 0; y < blockWidth; y++) {
                int color = iconTemplateTexture.getRGB(x, y);
                if(color == Color.RED.getRGB()) continue;
                if(color == Color.GREEN.getRGB()) icon.setRGB(x, y, borderColor.getRGB());
                if(color == Color.BLUE.getRGB()) icon.setRGB(x, y, baseTexture.getRGB(x, y));
            }

        ImageIO.write(icon, "png", new FsFile("./assets/editor/items/" + name + "/" + name + "-Icon.png", false).getFile());
    }

}
