package de.undefinedhuman.projectcreate.game.utils;

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
import de.undefinedhuman.projectcreate.core.ecs.equip.EquipComponent;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;
import de.undefinedhuman.projectcreate.game.entity.Entity;
import de.undefinedhuman.projectcreate.game.gui.Gui;
import de.undefinedhuman.projectcreate.game.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.game.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.game.inventory.player.Selector;
import de.undefinedhuman.projectcreate.game.world.World;
import de.undefinedhuman.projectcreate.game.world.layer.LayerTransition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

public class Tools extends de.undefinedhuman.projectcreate.engine.utils.Tools {

    public static boolean isMac = System.getProperty("os.name").contains("OS X");
    public static boolean isWindows = System.getProperty("os.name").contains("Windows");
    public static boolean isLinux = System.getProperty("os.name").contains("Linux");

    public static Random random = new Random();

    // private static Noise noise = new Noise(2, 1.7f, 0.4f);

    public static Vector2 getScreenPos(OrthographicCamera cam, Vector2 position) {
        return getScreenPos(cam, position.x, position.y);
    }

    public static Vector2 getScreenPos(OrthographicCamera cam, float x, float y) {
        Vector3 worldCoords = new Vector3(x, y, 0.0F);
        cam.project(worldCoords);
        return new Vector2(worldCoords.x, worldCoords.y);
    }

    public static Vector2 getWorldPos(OrthographicCamera cam, Vector2 pos) {
        return getWorldPos(cam, pos.x, pos.y);
    }

    public static Vector2 getWorldPos(OrthographicCamera cam, float x, float y) {
        Vector3 worldCoords = new Vector3(x, y, 0.0F);
        cam.unproject(worldCoords);
        return new Vector2(worldCoords.x, worldCoords.y);
    }

    public static Vector2 getMouseCoordsInWorldSpace(OrthographicCamera cam) {
        return getWorldPos(cam, Gdx.input.getX(), Gdx.input.getY());
    }

    public static Vector2 convertToWorldCoords(float x, float y) {
        Vector3 worldCoords = new Vector3(x, y, 0.0F);
        CameraManager.gameCamera.unproject(worldCoords);
        return new Vector2((int) (worldCoords.x / Variables.BLOCK_SIZE), (int) (worldCoords.y / Variables.BLOCK_SIZE));
    }

    public static Vector3 lerp(Vector3 vecFrom, Vector3 vecTo, float speed) {
        if ((vecFrom.x - vecTo.x > 750.0f) || (vecFrom.x - vecTo.x < -750.0f) || (vecFrom.y - vecTo.y > 750.0f) || (vecFrom.y - vecTo.y < -750.0f))
            return vecTo;
        return new Vector3(vecFrom.x + (vecTo.x - vecFrom.x) / (750.0f / speed + 5.0f) * Main.delta * 60.0f, vecFrom.y + (vecTo.y - vecFrom.y) / (750.0f / speed + 5.0f) * Main.delta * 60.0f, 0);
    }

    public static boolean isOverGuis(OrthographicCamera cam, Gui... guis) {
        for (Gui gui : guis) if (gui != null && gui.isVisible() && gui.isClicked(cam)) return true;
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

    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    public static void drawLine(SpriteBatch batch, Color col, float w, float x, float y, float x2, float y2) {
        Vector2 vec = new Vector2(x2 - x, y2 - y);
        Color bColor = batch.getColor();
        batch.setColor(col);
        batch.draw(new TextureRegion(TextureManager.getInstance().getTexture("blank.png")), x - w / 2, y, w / 2, 0, w, vec.len(), 1, 1, vec.angle() - 90);
        batch.setColor(bColor);
    }

    public static void drawLine(SpriteBatch batch, Texture tex, float x, float y, float x2, float y2, int bonus) {
        Vector2 vec = new Vector2(x2 - x, y2 - y);
        Sprite sprite = new Sprite(tex, 0, 0, tex.getWidth(), (int) vec.len() + bonus);
        sprite.setColor(batch.getColor());
        sprite.setOrigin(tex.getWidth() >> 1, 0);
        sprite.setPosition(x - (tex.getWidth() >> 1), y);
        sprite.setRotation(vec.angle() - 90);
        sprite.draw(batch);
    }

    public static float swordLerp(float x, float y, float speed) {
        if (y - x > 180) y -= 360;
        if (x - y > 180) x -= 360;
        return getResult(x, y, speed);
    }

    private static float getResult(float x, float y, float speed) {
        float t = (y - x) * speed * Main.delta;
        if (t < -40 * speed * Main.delta) t = -40 * speed * Main.delta;
        if (t > 40 * speed * Main.delta) t = 40 * speed * Main.delta;
        float result = x + t;
        while (result > 360) result -= 360;
        while (result < 0) result += 360;
        return result;
    }

    public static float swordLerpTurned(float x, float y, float speed, boolean turned) {
        while (turned && y < x) y += 360;
        while (!turned && y > x) y -= 360;
        if (Math.abs(x - y) < 0.3f) return y;
        return getResult(x, y, speed);
    }

    public static void drawRect(SpriteBatch batch, float x, float y, float w, float h, Color color) {
        Color batchColor = batch.getColor();
        batch.setColor(color);
        batch.draw(TextureManager.getInstance().getTexture("blank.png"), x, y, w, h);
        batch.setColor(batchColor);
    }

    public static boolean collideSAT(Sprite a, Vector2[] hitboxPoints) {
        Vector2[] aVectors = getVertices(a);
        Axis axis1 = new Axis(aVectors[1].x - aVectors[0].x, aVectors[1].y - aVectors[0].y), axis2 = new Axis(aVectors[1].x - aVectors[2].x, aVectors[1].y - aVectors[2].y);
        Axis axis3 = new Axis(hitboxPoints[0].x - hitboxPoints[3].x, hitboxPoints[0].y - hitboxPoints[3].y), axis4 = new Axis(hitboxPoints[0].x - hitboxPoints[1].x, hitboxPoints[0].y - hitboxPoints[1].y);
        return (collideAxis(aVectors, hitboxPoints, axis1) && collideAxis(aVectors, hitboxPoints, axis2) && collideAxis(aVectors, hitboxPoints, axis3) && collideAxis(aVectors, hitboxPoints, axis4));
    }

    public static Vector2[] getVertices(Sprite texture) {
        float[] vertices = texture.getVertices();
        return new Vector2[] {
                new Vector2(vertices[SpriteBatch.X1], vertices[SpriteBatch.Y1]),
                new Vector2(vertices[SpriteBatch.X2], vertices[SpriteBatch.Y2]),
                new Vector2(vertices[SpriteBatch.X3], vertices[SpriteBatch.Y3]),
                new Vector2(vertices[SpriteBatch.X4], vertices[SpriteBatch.Y4])
        };
    }

    private static boolean collideAxis(Vector2[] pointsA, Vector2[] pointsB, Axis axis) {
        Vector2[] axisAPoints = projectPointsOnAxis(axis, pointsA), axisBPoints = projectPointsOnAxis(axis, pointsB);
        float axis1MaxA = axis.getMaxPointOnAxis(axisAPoints), axis1MinA = axis.getMinPointOnAxis(axisAPoints), axis1MaxB = axis.getMaxPointOnAxis(axisBPoints), axis1MinB = axis.getMinPointOnAxis(axisBPoints);
        return axis1MinB <= axis1MaxA && axis1MaxB >= axis1MinA;
    }

    private static Vector2[] projectPointsOnAxis(Axis axis, Vector2[] vectors) {
        Vector2[] vecs = new Vector2[vectors.length];
        for (int i = 0; i < vecs.length; i++) vecs[i] = axis.projectPoint(vectors[i]);
        return vecs;
    }

    public static int floor(float f) {
        if (f >= 0) return (int) f;
        else return (int) f - 1;
    }

    public static void screenshot() {
        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);
        for (int i = 4; i < pixels.length; i += 4) pixels[i - 1] = (byte) 255;
        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
        PixmapIO.writePNG(Gdx.files.external(Paths.SCREENSHOT_PATH + "ScreenShot_" + getTime() + ".png"), pixmap);
        pixmap.dispose();
    }

    public static String getTime() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy - HH-mm-ss");
        Calendar cal = Calendar.getInstance();
        return df.format(cal.getTime());
    }

    public static boolean ctrl() {
        if (isMac) return Gdx.input.isKeyPressed(Input.Keys.SYM);
        else
            return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT);
    }

    public static boolean ctrl(int keycode) {
        if (isMac) return keycode == Input.Keys.SYM;
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

    public static Vector2i convertToBlockPos(Vector2 position) {
        return new Vector2i(position.x / Variables.BLOCK_SIZE, position.y / Variables.BLOCK_SIZE);
    }

    public static float mix(float a, float b, float f) {
        return a * (1 - f) + (b * f);
    }

    public static boolean compareVector(Vector2 a, Vector2 b) {
        if (a == null || b == null) return false;
        return (a.x == b.x) && (a.y == b.y);
    }

    public static Vector2 getScaledSize(Vector2 baseScale, Vector2 maxScale) {
        double widthRatio = maxScale.x / baseScale.x, heightRatio = maxScale.y / baseScale.y, ratio = Math.min(widthRatio, heightRatio);
        return new Vector2((int) (baseScale.x * ratio), (int) (baseScale.y * ratio));
    }

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }

    public static boolean contains(byte worldBlockID, byte[] blockIDs) {
        for (byte id : blockIDs) if (worldBlockID == id) return false;
        return true;
    }

    public static short getMostFrequentArrayElement(short[] array) {
        Arrays.sort(array);
        short maxID = array[0], currentID = array[0], maxAmount = 1, currentAmount = 1;
        for (int i = 1; i < array.length; i++) {
            if (currentID == array[i]) currentAmount++;
            else {
                if (currentAmount > maxAmount) {
                    maxAmount = currentAmount;
                    maxID = currentID;
                }
                currentAmount = 1;
            }
        }
        return maxID;
    }

    public static boolean getLayerTransitionMaxY(byte worldLayer, int x, int y, int maxY, LayerTransition trans) {

        switch (trans) {

            case TOP:
                return y > maxY && (World.instance.getBlock(x, y + 2, worldLayer) == 0 || World.instance.getBlock(x, y + 1, worldLayer) == 0);
            case SIN:
                return y < (maxY + (int) (Math.sin(x * 50) * 5));
            case RANDOM:
                return y < (maxY + random.nextInt(3) - 1);
            case CAVE:
                int tempY = maxY - y;
                return tempY < y;
            case LINEAR:
                return y < maxY;

        }

        return false;

    }

    public static boolean isItemSelected(Entity entity) {
        EquipComponent equipComponent;
        if ((equipComponent = (EquipComponent) entity.getComponent(EquipComponent.class)) != null || entity.mainPlayer)
            return (entity.mainPlayer ? Selector.getInstance().getSelectedInvItem() != null : equipComponent.itemIDs[0] != -1);
        return false;
    }

    public static int isEqual(int i, int j) {
        return i == j ? 1 : 0;
    }

    public static String appendSToString(int length) {
        return length > 1 ? "s" : "";
    }

    public static PixelConstraint getInventoryConstraint(GuiTemplate template, int size) {
        return new PixelConstraint(getInventorySize(template, size));
    }

    public static int getInventorySize(GuiTemplate template, int size) {
        return template.cornerSize * 2 + size * (Variables.SLOT_SIZE + Variables.SLOT_SPACE) - Variables.SLOT_SPACE;
    }

    public static int loop(int currentIndex, int maxLength) {
        return currentIndex == maxLength ? 0 : currentIndex;
    }

    public static int floorBackgroundSpeed(float speed) {
        return speed < 0f ? -1 : speed > 0 ? 1 : 0;
    }

    public static int calculateRandomValue(int value) {
        return Tools.random.nextInt(value) + value;
    }

}
