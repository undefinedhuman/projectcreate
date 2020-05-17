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
    private static Color borderColor = new Color(71, 35, 0);
    private static int templateCount = 16;
    private static int blockWidth = 16;

    public static void main(String[] args) throws IOException {

        Log.instance = new Log();
        BufferedImage baseTexture = ImageIO.read(new FsFile("./assets/editor/items/" + name + ".png", false).getFile());

        generateBlockTexture("./assets/editor/template/Block-Template.png", baseTexture, "./assets/editor/", borderColor);

        FileUtils.deleteFile(new FsFile("./assets/editor/items/" + name + "/Texture.atlas", false).getFile());
        FileUtils.deleteFile(new FsFile("./assets/editor/" + name + "/", true).getFile());

        ImageIO.write(generateIcon("./assets/editor/template/Sprite-Template-1.png", baseTexture, borderColor), "png", new FsFile("./assets/editor/items/" + name + "/" + name + "-Icon.png", false).getFile());

    }

    public static void generateBlockTexture(String templatePath, BufferedImage baseTexture, String tempTextureDir, Color borderColor) {
        BufferedImage templateTexture = null;
        try { templateTexture = ImageIO.read(new FsFile(templatePath, false).getFile()); } catch (IOException e) { Log.info("Loading template texture"); }
        if(templateTexture == null) return;
        for(int i = 0; i < templateCount; i++) {
            BufferedImage image = new BufferedImage(blockWidth, blockWidth, BufferedImage.TYPE_INT_ARGB);
            for(int x = 0; x < blockWidth; x++)
                for(int y = 0; y < blockWidth; y++) {
                    int tempX = i * blockWidth + x;
                    int color = templateTexture.getRGB(tempX, y);
                    if(color == Color.RED.getRGB()) continue;
                    if(color == Color.GREEN.getRGB()) image.setRGB(x, y, borderColor.getRGB());
                    if(color == Color.BLUE.getRGB()) image.setRGB(x, y, baseTexture.getRGB(x, y));
                }
            try { ImageIO.write(image, "png", new FsFile(tempTextureDir + name + "/" + i + ".png", false).getFile()); } catch (IOException e) { Log.info(e.getMessage()); }
        }

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
        TexturePacker.process(settings, tempTextureDir + name, tempTextureDir + "items/" + name + "/", "Texture");
    }

    public static BufferedImage generateIcon(String iconTemplatePath, BufferedImage baseTexture, Color borderColor) {
        BufferedImage icon = new BufferedImage(blockWidth, blockWidth, BufferedImage.TYPE_INT_ARGB);
        BufferedImage iconTemplateTexture;
        try { iconTemplateTexture = ImageIO.read(new FsFile(iconTemplatePath, false).getFile()); } catch (IOException e) { return null; }
        for(int x = 0; x < blockWidth; x++)
            for(int y = 0; y < blockWidth; y++) {
                int color = iconTemplateTexture.getRGB(x, y);
                if(color == Color.RED.getRGB()) continue;
                if(color == Color.GREEN.getRGB()) icon.setRGB(x, y, borderColor.getRGB());
                if(color == Color.BLUE.getRGB()) icon.setRGB(x, y, baseTexture.getRGB(x, y));
            }

        return icon;
    }

}
