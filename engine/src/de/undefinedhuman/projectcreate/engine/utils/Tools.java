package de.undefinedhuman.projectcreate.engine.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.FileReader;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObjectAdapter;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector4;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tools {

    public static void loadSettings(FileReader reader, SettingsList settings) {
        SettingsObject object = new SettingsObjectAdapter(reader);
        for(Setting<?> setting : settings.getSettings())
            setting.load(reader.parent(), object);
    }

    public static void saveSettings(FileWriter writer, SettingsList settings) {
        for(Setting<?> setting : settings.getSettings()) {
            setting.save(writer);
            writer.nextLine();
        }
    }

    public static String convertArrayToString(String[] array) {
        StringBuilder builder = new StringBuilder();
        for (String s : array)
            builder.append(s).append(Variables.SEPARATOR);
        return builder.toString();
    }

    public static String convertArrayToString(Vector2[] array) {
        StringBuilder builder = new StringBuilder();
        for (Vector2 current : array)
            builder.append(current.x).append(Variables.SEPARATOR).append(current.y).append(Variables.SEPARATOR);
        return builder.toString();
    }

    public static String convertArrayToPrintableString(String[] messages) {
        StringBuilder logMessage = new StringBuilder();
        for (int i = 0; i < messages.length; i++) logMessage.append(messages[i]).append(i < messages.length - 1 ? ", " : "");
        return logMessage.toString();
    }

    public static String convertArrayToPrintableString(Object[] messages) {
        return Arrays.stream(messages).map(Object::toString).collect(Collectors.joining(", "));
    }

    public static JPanel createSettingsPanel(Setting<?>... settings) {
        return createSettingsPanel(Variables.DEFAULT_CONTENT_WIDTH + Variables.BORDER_WIDTH, Variables.OFFSET, Stream.of(settings));
    }

    public static JPanel createSettingsPanel(Stream<Setting<?>> settings) {
        return createSettingsPanel(Variables.DEFAULT_CONTENT_WIDTH + Variables.BORDER_WIDTH, Variables.OFFSET, settings);
    }

    public static JPanel createSettingsPanel(int width, int offset, Stream<Setting<?>> settings) {
        JPanel panel = new JPanel();
        int currentHeight = 0;
        for(Setting<?> setting : settings.collect(Collectors.toList())) {
            JPanel settingsContainer = setting.createSettingUI(width);
            settingsContainer.setLocation(0, currentHeight);
            panel.add(settingsContainer);
            currentHeight += setting.getTotalHeight() + offset;
        }
        panel.setSize(width, currentHeight - offset);
        return panel;
    }

    public static void removeSettings(JComponent... panels) {
        for(JComponent panel : panels) {
            panel.removeAll();
            updatePanel(panel);
        }
    }

    public static void updatePanel(JComponent panel) {
        panel.revalidate();
        panel.repaint();
    }

    public static JTextField createTextField(String value, Vector2i position, Vector2i size, Consumer<String> keyReleaseEvent) {
        JTextField textField = new JTextField(value);
        textField.setBounds(position.x, position.y, size.x, size.y);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(textField.getText() == null || textField.getText().equalsIgnoreCase(""))
                    return;
                keyReleaseEvent.accept(textField.getText());
            }
        });
        return textField;
    }

    public static void drawLine(SpriteBatch batch, Vector2 point1, Vector2 point2, int lineWidth, Color color) {
        Vector2 vec = new Vector2(point2).sub(point1);
        TextureRegion texture = TextureManager.getInstance().getTexture("blank.png");
        Sprite sprite = new Sprite(texture, 0, 0, lineWidth, (int) vec.len());
        sprite.setColor(color);
        sprite.setOrigin(0, 0);
        sprite.setPosition(point1.x, point1.y);
        sprite.setRotation(vec.angle() - 90);
        sprite.draw(batch);
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

    public static String appendSToString(long length) {
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

    public static Float isFloat(String text) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException ex) { return null; }
    }

    public static Integer isInteger(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) { return null; }
    }

    public static Long isLong(String text) {
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException ex) { return null; }
    }

    public static Double isDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException ex) { return null; }
    }

    public static Byte isByte(String text) {
        try {
            return Byte.parseByte(text);
        } catch (NumberFormatException ex) { return null; }
    }

    public static Short isShort(String text) {
        try {
            return Short.parseShort(text);
        } catch (NumberFormatException ex) { return null; }
    }

    public static Vector2 getTextureSize(TextureRegion texture) {
        return new Vector2(texture.getRegionWidth(), texture.getRegionHeight());
    }

}
