package de.undefinedhuman.sandboxgame.engine.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.utils.math.Vector4;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tools {

    public static HashMap<String, LineSplitter> loadSettings(FileReader reader) {
        int size = reader.getNextInt();
        reader.nextLine();
        HashMap<String, LineSplitter> settings = new HashMap<>();
        for(int i = 0; i < size; i++) {
            settings.put(reader.getNextString(), reader.getRemainingRawData());
            reader.nextLine();
        }
        return settings;
    }

    public static void saveSettings(FileWriter writer, List<Setting> settings) {
        writer.writeInt(settings.size());
        writer.nextLine();
        for(Setting setting : settings) {
            setting.saveSetting(writer.getParentDirectory(), writer);
            writer.nextLine();
        }
    }

    public static String convertArrayToString(String[] array) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < array.length; i++) builder.append(array[i]).append(i < array.length-1 ? ";" : "");
        return builder.toString();
    }

    public static String convertArrayToString(Vector2[] array) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < array.length; i++) {
            Vector2 current = array[i];
            builder.append(current.x).append(";").append(current.y).append(i < array.length-1 ? ";" : "");
        }
        return builder.toString();
    }

    public static void addSettings(JPanel panel, ArrayList<Setting> settings) {
        int currentOffset = 0;
        for (Setting setting : settings) {
            setting.addMenuComponents(panel, new Vector2(0, currentOffset));
            currentOffset += setting.offset;
        }
        resetPanel(panel);
    }

    public static void removeSettings(JPanel panel) {
        panel.removeAll();
        resetPanel(panel);
    }

    public static void resetPanel(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static boolean isInRange(int val, int min, int max) {
        return val >= min && val <= max;
    }

    public static boolean isInRange(float val, float min, float max) {
        return val >= min && val <= max;
    }

    public static TextureRegion fixBleeding(TextureRegion region) {
        float fix = 0.1f, x = region.getRegionX(), y = region.getRegionY(), width = region.getRegionWidth(), height = region.getRegionHeight(), invTexWidth = 1f / region.getTexture().getWidth(), invTexHeight = 1f / region.getTexture().getHeight();
        region.setRegion((x + fix) * invTexWidth, (y + fix) * invTexHeight, (x + width - fix) * invTexWidth, (y + height - fix) * invTexHeight);
        return region;
    }

    public static int isEqual(int i, int j) {
        return i == j ? 1 : 0;
    }

    public static String appendSToString(int length) {
        return length > 1 ? "s" : "";
    }

    public static BufferedImage scaleNearest(BufferedImage before, double scale) {
        return scale(before, scale, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    }

    private static BufferedImage scale(final BufferedImage before, double scale, int type) {
        int w = before.getWidth(), h = before.getHeight(), w2 = (int) (w * scale), h2 = (int) (h * scale);
        BufferedImage after = new BufferedImage(w2, h2, before.getType());
        AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, type);
        scaleOp.filter(before, after);
        return after;
    }

    public static int getWorldPositionX(float x, float width) {
        return (int) ((width + x) % width);
    }

    public static Vector4 calculateBounds(Vector2 position, Vector2 offset, Vector2 size) {
        return new Vector4(position.x + offset.x, position.y + offset.y, position.x + offset.x + size.x, position.y + offset.y + size.y);
    }

    public static boolean isDigit(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static void saveExpectedImage(BufferedImage image, String name) {
        try {
            ImageIO.write(image, "png", new FsFile("test/editorUI/" + name + "_Expected.png", false).getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Vector2 getTextureSize(TextureRegion texture) {
        return new Vector2(texture.getRegionWidth(), texture.getRegionHeight());
    }


}
