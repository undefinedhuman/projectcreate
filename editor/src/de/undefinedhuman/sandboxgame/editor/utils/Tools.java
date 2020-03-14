package de.undefinedhuman.sandboxgame.editor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.undefinedhuman.sandboxgame.editor.Main;
import de.undefinedhuman.sandboxgame.editor.engine.ressources.TextureManager;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Tools {

    public static boolean isMac = System.getProperty("os.name").contains("OS X");
    public static boolean isWindows = System.getProperty("os.name").contains("Windows");
    public static boolean isLinux = System.getProperty("os.name").contains("Linux");

    public static String getTime() {

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy - HH-mm-ss");
        Calendar cal = Calendar.getInstance();

        return df.format(cal.getTime());

    }

    public static Vector2 getScreenPos(OrthographicCamera cam, float x, float y) {

        Vector3 worldCoord = new Vector3(x, y, 0.0F);
        cam.project(worldCoord);
        return new Vector2(worldCoord.x, worldCoord.y);

    }

    public static Vector2 getWorldPos(OrthographicCamera cam, float x, float y) {

        Vector3 worldCoord = new Vector3(x, y, 0.0F);
        cam.unproject(worldCoord);
        return new Vector2(worldCoord.x, worldCoord.y);

    }

    public static Vector2 getWorldPos(OrthographicCamera cam, Vector2 pos) {

        Vector3 worldCoord = new Vector3(pos.x, pos.y,0.0F);
        worldCoord = cam.unproject(worldCoord);
        return new Vector2(worldCoord.x, worldCoord.y);

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

    public static boolean isClicked(Sprite spr, OrthographicCamera cam) {

        Vector2 coord = getAbsoluteMouse(cam);
        return coord.x >= spr.getX() && coord.y >= spr.getY() && coord.x <= spr.getX() + spr.getWidth() && coord.y <= spr.getY() + spr.getHeight();

    }

    public static boolean isClicked(Sprite[] sprites, OrthographicCamera cam) {

        Vector2 coord = getAbsoluteMouse(cam);
        boolean clicked = false;
        for(Sprite spr : sprites) if(coord.x >= spr.getX() && coord.y >= spr.getY() && coord.x <= spr.getX() + spr.getWidth() && coord.y <= spr.getY() + spr.getHeight() && !clicked) clicked = true;
        return clicked;

    }

    public static boolean isClicked(ArrayList<Sprite> sprites, OrthographicCamera cam) {

        Vector2 coord = getAbsoluteMouse(cam);
        boolean clicked = false;
        for(Sprite spr : sprites) if(spr.getBoundingRectangle().contains(coord) && !clicked) clicked = true;
        return clicked;

    }

    public static boolean isClicked(Vector2 point1, Vector2 start, Vector2 size) {

        return (point1.x >= start.x && point1.x <= start.x + size.x) && (point1.y >= start.y && point1.y <= start.y + size.y);

    }

    public static float cosInterpolate(float a, float b, float blend) {

        double ft = blend * 3.141592653589793D;
        float f = (float)((1.0D - Math.cos(ft)) * 0.5D);
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

        Vector2 newPoint = new Vector2();
        newPoint.x = temp.x * c - temp.y * s;
        newPoint.y = temp.x * s + temp.y * c;

        point = new Vector2(newPoint).add(center);
        return point;

    }

    public static boolean isDifferentEnough(float a, float b, float c, float d, float e) {

        if(Math.abs(a - b) > 0.02F) { return true; } if(Math.abs(a - c) > 0.02F) { return true; } if(Math.abs(a - d) > 0.02F) { return true; }
        return Math.abs(a - e) > 0.02F;

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

        float result;

        float t = (y - x) * speed * Main.delta;
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

    public static Vector2[] getVertices(Sprite texture) {

        float[] vertices = texture.getVertices();

        Vector2[] vec = new Vector2[4];
        vec[0] = new Vector2(vertices[SpriteBatch.X1], vertices[SpriteBatch.Y1]);
        vec[1] = new Vector2(vertices[SpriteBatch.X2], vertices[SpriteBatch.Y2]);
        vec[2] = new Vector2(vertices[SpriteBatch.X3], vertices[SpriteBatch.Y3]);
        vec[3] = new Vector2(vertices[SpriteBatch.X4], vertices[SpriteBatch.Y4]);

        return vec;

    }

    public static void fixBleeding(TextureRegion region) {

        float fix = 0.1f;
        float x = region.getRegionX();
        float y = region.getRegionY();
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float invTexWidth = 1f / region.getTexture().getWidth();
        float invTexHeight = 1f / region.getTexture().getHeight();
        region.setRegion((x + fix) * invTexWidth, (y + fix) * invTexHeight, (x + width - fix) * invTexWidth, (y + height - fix) * invTexHeight);

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
        } catch (NumberFormatException ex) {
            return false;
        }

    }

    public static float max(float x, float y) {
        return x >= y ? x : y;
    }

    public static boolean compareVector(Vector2 a, Vector2 b) {
        if(a == null || b == null) return false;
        return (a.x == b.x) && (a.y == b.y);
    }

    public static Vector2 getScaledSize(Vector2 baseScale, Vector2 maxScale) {
        double widthRatio = maxScale.x / baseScale.x;
        double heightRatio = maxScale.y / maxScale.y;
        double ratio = Math.min(widthRatio, heightRatio);

        return new Vector2((int) (baseScale.x * ratio), (int) (baseScale.y * ratio));
    }

    public static BufferedImage scaleNearest(BufferedImage before, double scale) {
        final int interpolation = AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
        return scale(before, scale, interpolation);
    }

    private static BufferedImage scale(final BufferedImage before, final double scale, final int type) {
        int w = before.getWidth(), h = before.getHeight(), w2 = (int) (w * scale), h2 = (int) (h * scale);
        BufferedImage after = new BufferedImage(w2, h2, before.getType());
        AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, type);
        scaleOp.filter(before, after);
        return after;
    }

}
