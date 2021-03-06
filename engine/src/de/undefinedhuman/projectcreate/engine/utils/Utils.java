package de.undefinedhuman.projectcreate.engine.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.undefinedhuman.projectcreate.engine.file.FileReader;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObjectFileReader;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Utils {

    public static void loadSettings(FileReader reader, SettingsList settings) {
        SettingsObject object = new SettingsObjectFileReader(reader);
        for(Setting<?> setting : settings.getSettings())
            setting.load(reader.parent(), object);
    }

    public static void saveSettings(FileWriter writer, SettingsList settings) {
        for(Setting<?> setting : settings.getSettings()) {
            setting.save(writer);
            writer.nextLine();
        }
    }

    public static String convertArrayToPrintableString(Object[] messages) {
        return Arrays.stream(messages).map(Object::toString).collect(Collectors.joining(", "));
    }

    public static JTextField createTextField(String key, String value, Consumer<String> keyReleaseEvent, boolean canBeEmpty, String defaultValue) {
        JTextField textField = new JTextField(value);
        textField.setFont(textField.getFont().deriveFont(16f).deriveFont(Font.BOLD));
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(textField.getText() == null || (!canBeEmpty && textField.getText().equalsIgnoreCase(""))) {
                    return;
                }
                keyReleaseEvent.accept(textField.getText());
            }
        });
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if(canBeEmpty || !textField.getText().equalsIgnoreCase(""))
                    return;
                keyReleaseEvent.accept(defaultValue);
                textField.setText(defaultValue);
                Log.warn(key + " should not be empty!");
            }
        });
        return textField;
    }

    public static void drawLine(SpriteBatch batch, Vector2 point1, Vector2 point2, float lineWidth, Color color) {
        Vector2 vec = new Vector2(point2).sub(point1);
        TextureRegion texture = TextureManager.getInstance().getTexture("blank.png");
        Sprite sprite = new Sprite(texture);
        sprite.setBounds(point1.x, point1.y, lineWidth, vec.len());
        sprite.setSize(lineWidth, vec.len());
        sprite.setColor(color);
        sprite.setOrigin(0, 0);
        sprite.setPosition(point1.x, point1.y);
        sprite.setRotation(vec.angleDeg() - 90);
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

    public static <T> boolean hasValue(T[] values, T value) {
        for(T s : values)
            if(s.equals(value))
                return true;
        return false;
    }

    private static final Vector3 TEMP_VECTOR3 = new Vector3();

    public static Vector2 calculateScreenFromWorldSpace(OrthographicCamera camera, Vector2 worldPosition) {
        return calculateScreenFromWorldSpace(camera, worldPosition.x, worldPosition.y);
    }

    public static Vector2 calculateScreenFromWorldSpace(OrthographicCamera camera, float worldX, float worldY) {
        TEMP_VECTOR3.set(worldX, worldY, 0);
        camera.project(TEMP_VECTOR3);
        return new Vector2(TEMP_VECTOR3.x, TEMP_VECTOR3.y);
    }

    public static Vector2 calculateWorldFromScreenSpace(OrthographicCamera camera, Vector2 screenPosition) {
        return calculateWorldFromScreenSpace(camera, screenPosition.x, screenPosition.y);
    }

    public static Vector2 calculateWorldFromScreenSpace(OrthographicCamera camera, float screenX, float screenY) {
        TEMP_VECTOR3.set(screenX, screenY, 0);
        camera.unproject(TEMP_VECTOR3);
        return new Vector2(TEMP_VECTOR3.x, TEMP_VECTOR3.y);
    }

    public static int countChar(String input, char c) {
        int count = 0;
        for(int i = 0; i < input.length(); i++)
            if(input.charAt(i) == c)
                count++;
        return count;
    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

}
