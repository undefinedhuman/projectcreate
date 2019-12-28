package de.undefinedhuman.sandboxgame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.file.FileUtils;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.gui.Gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class Tools {

    public static boolean isMac = System.getProperty("os.name").contains("OS X");
    public static boolean isWindows = System.getProperty("os.name").contains("Windows");
    public static boolean isLinux = System.getProperty("os.name").contains("Linux");
    public static Random random = new Random();

    public static String getTime() {

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy - HH-mm-ss");
        Calendar cal = Calendar.getInstance();

        return df.format(cal.getTime());

    }

    public static Vector2 getScreenPos(OrthographicCamera cam, Vector2 position) {
        return getScreenPos(cam, position.x, position.y);
    }

    public static Vector2 getScreenPos(OrthographicCamera cam, float x, float y) {

        Vector3 worldCoords = new Vector3(x, y, 0.0F);
        cam.project(worldCoords);
        return new Vector2(worldCoords.x, worldCoords.y);

    }

    public static Vector2 getWorldPos(OrthographicCamera cam, float x, float y) {

        Vector3 worldCoords = new Vector3(x, y, 0.0F);
        cam.unproject(worldCoords);
        return new Vector2(worldCoords.x, worldCoords.y);

    }

    public static Vector2 getWorldPos(OrthographicCamera cam, Vector2 pos) {

        Vector3 worldCoords = new Vector3(pos.x, pos.y,0.0F);
        worldCoords = cam.unproject(worldCoords);
        return new Vector2(worldCoords.x, worldCoords.y);

    }

    public static Vector2 getAbsoluteMouse(OrthographicCamera cam) {
        return getAbsolutePos(cam, Gdx.input.getX(), Gdx.input.getY());
    }

    public static Vector2 getAbsolutePos(OrthographicCamera cam, float x, float y) {

        Vector3 worldCoord = new Vector3(x, y, 0.0F);
        cam.unproject(worldCoord);
        return new Vector2(worldCoord.x, worldCoord.y);

    }

    public static Vector3 lerp(Vector3 vecFrom, Vector3 vecTo, float speed) {
        if ((vecFrom.x - vecTo.x > 750.0f) || (vecFrom.x - vecTo.x < -750.0f) || (vecFrom.y - vecTo.y > 750.0f) || (vecFrom.y - vecTo.y < -750.0f))
            return vecTo;
        return new Vector3(vecFrom.x + (vecTo.x - vecFrom.x) / (750.0f / speed + 5.0f) * Main.delta * 60.0f, vecFrom.y + (vecTo.y - vecFrom.y) / (750.0f / speed + 5.0f) * Main.delta * 60.0f, 0);
    }

    public static boolean isOverGuis(OrthographicCamera cam, Gui... guis) {
        for(Gui gui : guis) if(gui != null && gui.isVisible() && gui.isClicked(cam)) return true;
        return false;
    }

    public static float cosInterpolate(float a, float b, float blend) {
        double ft = blend * 3.141592653589793D;
        float f = (float) ((1.0D - Math.cos(ft)) * 0.5D);
        return a * (1.0F - f) + b * f;
    }

    public static float getAngle(Vector2 base, Vector2 target) {
        float angle = (float) Math.toDegrees(Math.atan2(target.y - base.y, target.x - base.x));
        if (angle < 0.0F) angle += 360.0F;
        return angle;
    }

    public static Vector2 rotatePoint(Vector2 point, Vector2 center, float angle) {
        float s = (float) Math.sin(angle), c = (float) Math.cos(angle);
        Vector2 temp = new Vector2(point).sub(center);
        Vector2 newPoint = new Vector2(temp.x * c - temp.y * s, temp.x * s + temp.y * c);
        point = new Vector2(newPoint).add(center);
        return point;
    }

    public static boolean isDifferentEnough(float a, float b, float c, float d, float e) {
        if(Math.abs(a - b) > 0.02F) { return true; } if(Math.abs(a - c) > 0.02F) { return true; } if(Math.abs(a - d) > 0.02F) { return true; }
        return Math.abs(a - e) > 0.02F;
    }

    public static float lerp(float a, float b, float f) {
        return a * (1 - f) + (b * f);
    }

    public static void drawLine(SpriteBatch batch, Color col, float w, float x, float y, float x2, float y2) {
        Vector2 vec = new Vector2(x2-x,y2-y);
        Color bColor = batch.getColor();
        batch.setColor(col);
        batch.draw(new TextureRegion(TextureManager.instance.getTexture("blank.png")), x-w/2, y, w/2, 0, w, vec.len(), 1, 1, vec.angle()-90);
        batch.setColor(bColor);
    }

    public static void drawLine(SpriteBatch batch, Texture tex, float x, float y, float x2, float y2, int bonus) {
        Vector2 vec = new Vector2(x2-x,y2-y);
        Sprite sprite = new Sprite(tex, 0, 0, tex.getWidth(), (int) vec.len()+bonus);
        sprite.setColor(batch.getColor());
        sprite.setOrigin(tex.getWidth()/2, 0);
        sprite.setPosition(x-tex.getWidth()/2, y);
        sprite.setRotation( vec.angle()-90);
        sprite.draw(batch);
    }

    public static float swordLerp(float x, float y, float speed) {
        if(y - x > 180) y -= 360;
        if(x - y > 180) x -= 360;
        float t = (y - x) * speed * Main.delta;
        if(t < -40 * speed * Main.delta) t = -40 * speed * Main.delta;
        if(t > 40 * speed*Main.delta) t = 40 * speed * Main.delta;
        float result = x + t;
        while(result > 360) result -= 360;
        while(result < 0) result += 360;
        return result;
    }

    public static float swordLerpTurned(float x, float y, float speed, boolean turned) {
        while (turned && y < x) y += 360;
        while (!turned && y > x) y -= 360;
        if(Math.abs(x - y) < 0.3f) return y;
        float result, t = (y - x) * speed * Main.delta;
        if (t < -40 * speed * Main.delta) t = -40 * speed * Main.delta;
        else if (t > 40 * speed * Main.delta) t = 40 * speed * Main.delta;
        result = x + t;
        while(result > 360) result -= 360;
        while(result < 0) result += 360;
        return result;
    }

    public static Vector2[] getPoints(Vector2 positon, Vector2 size, float angle, Vector2 center) {
        Vector2[] points = new Vector2[4];
        points[0] = positon;
        points[1] = new Vector2(positon).add(size.x, 0);
        points[2] = new Vector2(positon).add(0, size.y);
        points[3] = new Vector2(positon).add(size);
        for (Vector2 point : points) Tools.rotatePoint(point, center, angle);
        return points;
    }

    public static void drawRect(SpriteBatch batch, float x, float y, float w, float h, Color color) {
        Color batchColor = batch.getColor();
        batch.setColor(color);
        batch.draw(TextureManager.instance.getTexture("blank.png"), x, y, w, h);
        batch.setColor(batchColor);
    }

    public static void drawRect(SpriteBatch batch, Texture texture, float x, float y, float w, float h) {
        batch.draw(texture, x, y, w, h);
    }

    public static void drawRect(SpriteBatch batch, Texture texture, float x, float y, float w, float h, Color color) {
        Color batchColor = batch.getColor();
        batch.setColor(color);
        batch.draw(texture, x, y, w, h);
        batch.setColor(batchColor);
    }

    public static void drawRect(SpriteBatch batch, Texture texture, float x, float y, float w, float h, Color color, float alpha) {
        Color batchColor = batch.getColor();
        batch.setColor(color.r, color.g, color.b, alpha);
        batch.draw(TextureManager.instance.getTexture("blank.png"), x, y, w, h);
        batch.setColor(batchColor);
    }

    public static void drawRect(SpriteBatch batch, Texture texture, float x, float y, float w, float h, float alpha) {
        Color batchColor = batch.getColor();
        batch.setColor(batchColor.r, batchColor.g, batchColor.b, alpha);
        batch.draw(TextureManager.instance.getTexture("blank.png"), x, y, w, h);
        batch.setColor(batchColor);
    }

    public static int floor(float f) {
        if(f >= 0) return (int) f;
        else return (int) f-1;
    }

    public static float angleCrop(float angle) {
        while(angle<0) angle += 360;
        while(angle>360) angle -= 360;
        return angle;
    }

    /*public static boolean collideSAT(Sprite spriteA, Vector2[] verticesB) {

        Vector2[] verticesA = getVertices(spriteA);

        for (int i = 0; i < verticesA.length; i++) {
            Vector2 A = verticesA[i], B = verticesA[(i + 1) % verticesA.length], normal = new Vector2(B.x - A.x, B.y - A.y).rotate90(-1);
            if (intersects(verticesA, verticesB, normal)) return false;
        }

        for (int i = 0; i < verticesB.length; i++) {
            Vector2 A = verticesB[i], B = verticesB[(i + 1) % verticesB.length], normal = new Vector2(B.x - A.x, B.y - A.y).rotate90(-1);
            if (intersects(verticesA, verticesB, normal)) return false;
        }

        return true;

    }


    public static boolean collideSAT(Vector2[] verticesA, Vector2[] verticesB) {

        for (int i = 0; i < verticesA.length; i++) {
            Vector2 A = verticesA[i], B = verticesA[(i + 1) % verticesA.length], normal = new Vector2(B.x - A.x, B.y - A.y).rotate90(-1);
            if (intersects(verticesA, verticesB, normal)) return false;
        }

        for (int i = 0; i < verticesB.length; i++) {
            Vector2 A = verticesB[i], B = verticesB[(i + 1) % verticesB.length], normal = new Vector2(B.x - A.x, B.y - A.y).rotate90(-1);
            if (intersects(verticesA, verticesB, normal)) return false;
        }

        return true;

    }

    private static boolean intersects(Vector2[] verticesA, Vector2[] verticesB, Vector2 axis) {

        float minA = Float.MAX_VALUE, maxA = Float.MIN_VALUE;
        for (Vector2 vertex : verticesA) {
            float value = vertex.dot(axis);
            if (value < minA) minA = value;
            if (value > maxA) maxA = value;
        }

        float minB = Float.MAX_VALUE, maxB = Float.MIN_VALUE;
        for (Vector2 vertex : verticesB) {
            float value = vertex.dot(axis);
            if (value < minB) minB = value;
            if (value > maxB) maxB = value;
        }

        return minA > maxB || minB > maxA;
    }*/

    public static void screenshot() {
        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);
        for (int i = 4; i < pixels.length; i += 4) pixels[i - 1] = (byte) 255;
        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
        PixmapIO.writePNG(Gdx.files.external(Paths.SCREENSHOT_PATH.getPath() + "ScreenShot_" + Tools.getTime() + ".png"), pixmap);
        pixmap.dispose();
    }

    public static TextureRegion fixBleeding(TextureRegion region) {
        float fix = 0.1f, x = region.getRegionX(), y = region.getRegionY(), width = region.getRegionWidth(), height = region.getRegionHeight(), invTexWidth = 1f / region.getTexture().getWidth(), invTexHeight = 1f / region.getTexture().getHeight();
        region.setRegion((x + fix) * invTexWidth, (y + fix) * invTexHeight, (x + width - fix) * invTexWidth, (y + height - fix) * invTexHeight);
        return region;
    }

    public static boolean ctrl() {
        if(isMac) return Gdx.input.isKeyPressed(Input.Keys.SYM);
        else return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT);
    }

    public static boolean ctrl(int keycode) {
        if(isMac) return keycode == Input.Keys.SYM;
        else return keycode == Input.Keys.CONTROL_LEFT || keycode == Input.Keys.CONTROL_RIGHT;
    }

    public static boolean alt() {
        return Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT);
    }

    public static boolean alt(int keycode) {
        return keycode == Input.Keys.ALT_LEFT || keycode == Input.Keys.ALT_RIGHT;
    }

    public static boolean shift() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }

    public static boolean shift(int keycode) {
        return keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT;
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

    public static Vector2 convertToWorldCoord(Vector2 pos) {
        return new Vector2(pos.x / 16, pos.y / 16);
    }

    public static float mix(float a, float b, float f) {
        return a * (1 - f) + (b * f);
    }

    public static boolean isDigit(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException ex) { return false; }
    }

    public static boolean compareVector(Vector2 a, Vector2 b) {
        if(a == null || b == null) return false;
        return (a.x == b.x) && (a.y == b.y);
    }

    public static Vector2 getScaledSize(Vector2 baseScale, Vector2 maxScale) {
        double widthRatio = maxScale.x / baseScale.x, heightRatio = maxScale.y / maxScale.y, ratio = Math.min(widthRatio, heightRatio);
        return new Vector2((int) (baseScale.x * ratio), (int) (baseScale.y * ratio));
    }

    public static String loadString(HashMap<String, LineSplitter> settings, String name, String defaultValue) {
        if(settings.containsKey(name)) return settings.get(name).getNextString();
        return defaultValue;
    }

    public static String[] loadStringArray(HashMap<String, LineSplitter> settings, String name, String defaultValue) {

        if(settings.containsKey(name)) {

            LineSplitter s = settings.get(name);
            String[] strings = new String[s.getNextInt()];
            for(int i = 0; i < strings.length; i++) strings[i] = s.getNextString();
            return strings;

        }

        return new String[0];

    }

    public static float loadFloat(HashMap<String, LineSplitter> settings, String name, Object defaultValue) {
        return Float.parseFloat(loadString(settings, name, String.valueOf(defaultValue)));
    }

    public static int loadInt(HashMap<String, LineSplitter> settings, String name, Object defaultValue) {
        return Integer.parseInt(loadString(settings, name, String.valueOf(defaultValue)));
    }

    public static double loadDouble(HashMap<String, LineSplitter> settings, String name, Object defaultValue) {
        return Double.parseDouble(loadString(settings, name, String.valueOf(defaultValue)));
    }

    public static Vector2 loadVector2(HashMap<String, LineSplitter> settings, String name, Vector2 defaultValue) {
        return new Vector2(loadFloat(settings, name, defaultValue.x), loadFloat(settings, name, defaultValue.y));
    }

    public static Vector2[] loadVector2Array(HashMap<String, LineSplitter> settings, String name, Vector2 defaultValue) {

        if(settings.containsKey(name)) {

            LineSplitter s = settings.get(name);
            Vector2[] vectors = new Vector2[s.getNextInt()];
            for(int i = 0; i < vectors.length; i++) vectors[i] = s.getNextVector2();
            return vectors;

        }

        return new Vector2[0];

    }

    public static Vector3 loadVector3(HashMap<String, LineSplitter> settings, String name, Vector3 defaultValue) {
        return new Vector3(loadFloat(settings, name, defaultValue.x), loadFloat(settings, name, defaultValue.y), loadFloat(settings, name, defaultValue.z));
    }

    public static boolean loadBoolean(HashMap<String, LineSplitter> settings, String name, Object defaultValue) {
        return FileUtils.readBoolean(loadString(settings, name, String.valueOf(defaultValue)));
    }

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }

    public static boolean contains(byte worldBlockID, byte[] blockIDs) {
        for(byte id : blockIDs) if(worldBlockID == id) return false;
        return true;
    }

    public static byte getMostFrequentArrayElement(byte[] array) {
        Arrays.sort(array);
        byte maxID = array[0], currentID = array[0], maxAmount = 1, currentAmount = 1;
        for(int i = 1; i < array.length; i++) {
            if (currentID == array[i]) currentAmount++;
            else {
                if (currentAmount > maxAmount) { maxAmount = currentAmount; maxID = currentID; }
                currentAmount = 1;
            }
        }
        return maxID;
    }


}
