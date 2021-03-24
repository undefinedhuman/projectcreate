package de.undefinedhuman.projectcreate.editor;

@SuppressWarnings("DuplicatedCode")
public class BlockTextureGenerator {

    /*private static String NAME = "Dirt";
    private static Color BORDER_COLOR = new Color(71, 35, 0);
    private static int TEMPLATE_COUNT = 16;

    public static void main(String[] args) throws IOException {

        Log.instance = new Log();
        BufferedImage baseTexture = ImageIO.read(new FsFile("./assets/editor/items/" + NAME + ".png", false).getFile());

        generateBlockTexture("./assets/editor/template/Block-Template.png", baseTexture, "./assets/editor/", BORDER_COLOR);

        FileUtils.deleteFile(new FsFile("./assets/editor/items/" + NAME + "/Texture.atlas", false));
        FileUtils.deleteFile(new FsFile("./assets/editor/" + NAME + "/", true));

        BufferedImage icon = generateIcon("./assets/editor/template/Sprite-Template-6.png", baseTexture, BORDER_COLOR);
        if(icon == null) {
            System.out.println("Generated Icon doesn't exist!");
            return;
        }
        ImageIO.write(icon, "png", new FsFile("./assets/editor/items/" + NAME + "/" + NAME + "-Icon.png", false).getFile());

        BufferedImage preview = generateIcon("./assets/editor/template/Sprite-Template-6.png", baseTexture, BORDER_COLOR);
        if(preview == null) {
            System.out.println("Generated Icon doesn't exist!");
            return;
        }
        ImageIO.write(preview, "png", new File("./assets/editor/items/" + NAME + "/" + NAME + "-Icon.png", false).createNewFile());

    }

    public static void generateBlockTexture(String templatePath, BufferedImage baseTexture, String tempTextureDir, Color borderColor) {
        BufferedImage templateTexture = null;
        try { templateTexture = ImageIO.read(new FsFile(templatePath, false).getFile()); } catch (IOException e) { Log.info("Loading template texture"); }
        if(templateTexture == null) return;
        for(int i = 0; i < TEMPLATE_COUNT; i++) {
            BufferedImage image = new BufferedImage(Variables.BLOCK_SIZE, Variables.BLOCK_SIZE, BufferedImage.TYPE_INT_ARGB);
            for(int x = 0; x < Variables.BLOCK_SIZE; x++)
                for(int y = 0; y < Variables.BLOCK_SIZE; y++) {
                    int tempX = i * Variables.BLOCK_SIZE + x;
                    int color = templateTexture.getRGB(tempX, y);
                    if(color == Color.RED.getRGB()) continue;
                    if(color == Color.GREEN.getRGB()) image.setRGB(x, y, borderColor.getRGB());
                    if(color == Color.BLUE.getRGB()) image.setRGB(x, y, baseTexture.getRGB(x, y));
                }
            try { ImageIO.write(image, "png", new FsFile(tempTextureDir + NAME + "/" + i + ".png", false).getFile()); } catch (IOException e) { Log.info(e.getMessage()); }
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
        settings.pot = false;
        settings.alias = true;
        settings.ignoreBlankImages = false;
        settings.debug = false;
        settings.useIndexes = false;
        settings.premultiplyAlpha = false;
        settings.limitMemory = false;
        TexturePacker.process(settings, tempTextureDir + NAME, tempTextureDir + "items/" + NAME + "/", "Texture");
    }

    public static BufferedImage generateIcon(String iconTemplatePath, BufferedImage baseTexture, Color borderColor) {
        BufferedImage icon = new BufferedImage(Variables.BLOCK_SIZE, Variables.BLOCK_SIZE, BufferedImage.TYPE_INT_ARGB);
        BufferedImage iconTemplateTexture;
        try { iconTemplateTexture = ImageIO.read(new FsFile(iconTemplatePath, false).getFile()); } catch (IOException e) { return null; }
        for(int x = 0; x < Variables.BLOCK_SIZE; x++)
            for(int y = 0; y < Variables.BLOCK_SIZE; y++) {
                int color = iconTemplateTexture.getRGB(x, y);
                if(color == Color.RED.getRGB()) continue;
                if(color == Color.GREEN.getRGB()) icon.setRGB(x, y, borderColor.getRGB());
                if(color == Color.BLUE.getRGB()) icon.setRGB(x, y, baseTexture.getRGB(x, y));
            }

        return icon;
    }

    public static BufferedImage generatePreview(String iconTemplatePath, BufferedImage baseTexture, Color borderColor) {
        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        BufferedImage iconTemplateTexture;
        try { iconTemplateTexture = ImageIO.read(new FsFile(iconTemplatePath, false).getFile()); } catch (IOException e) { return null; }
        int base = 16 - Variables.BLOCK_SIZE/2;
        for(int x = 0; x < Variables.BLOCK_SIZE; x++)
            for(int y = 0; y < Variables.BLOCK_SIZE; y++) {
                int color = iconTemplateTexture.getRGB(x, y);
                if(color == Color.RED.getRGB()) continue;
                if(color == Color.GREEN.getRGB()) icon.setRGB(base + x, base + y, borderColor.getRGB());
                if(color == Color.BLUE.getRGB()) icon.setRGB(base + x, base + y, baseTexture.getRGB(x, y));
            }

        return icon;
    }*/

}
