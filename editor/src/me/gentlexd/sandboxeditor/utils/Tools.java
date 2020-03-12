package me.gentlexd.sandboxeditor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.gentlexd.sandboxeditor.Main;
import me.gentlexd.sandboxeditor.engine.ressources.TextureManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Tools {

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

    public static Vector2 getAbsoluteMouse(OrthographicCamera cam) {

        return getAbsolutePos(cam, Gdx.input.getX(), Gdx.input.getY());

    }

    public static Vector2 getAbsolutePos(OrthographicCamera cam, float x, float y) {

        Vector3 worldCoord = new Vector3(x, y, 0.0F);
        cam.unproject(worldCoord);
        return new Vector2(worldCoord.x, worldCoord.y);

    }

    public static Vector3 lerp(Vector3 vecFrom, Vector3 vecTo, float speed) {

        if ((vecFrom.x - vecTo.x > 1000.0F) || (vecFrom.x - vecTo.x < -1000.0F) || (vecFrom.y - vecTo.y > 1000.0F) || (vecFrom.y - vecTo.y < -1000.0F)) {
            return vecTo;
        }

        return new Vector3(vecFrom.x + (vecTo.x - vecFrom.x) / (1000.0F / speed + 5.0F) * Main.delta * 60.0F, vecFrom.y + (vecTo.y - vecFrom.y) / (1000.0F / speed + 5.0F) * Main.delta * 60.0F, 0.0F);

    }

    public static boolean isClicked(Sprite spr, OrthographicCamera cam) {

        Vector2 coord = getAbsoluteMouse(cam);
        return coord.x >= spr.getX() && coord.y >= spr.getY() && coord.x <= spr.getX() + spr.getWidth() && coord.y <= spr.getY() + spr.getHeight();

    }

    public static boolean isClicked(Sprite[] sprites, OrthographicCamera cam) {

        Vector2 coord = getAbsoluteMouse(cam);

        boolean clicked = false;

        for(Sprite spr : sprites) {

            if(coord.x >= spr.getX() && coord.y >= spr.getY() && coord.x <= spr.getX() + spr.getWidth() && coord.y <= spr.getY() + spr.getHeight()) {

                if(!clicked) {

                    clicked = true;

                }

            }

        }

        return clicked;

    }

    public static boolean isClicked(ArrayList<Sprite> sprites, OrthographicCamera cam) {

        Vector2 coord = getAbsoluteMouse(cam);

        boolean clicked = false;

        for(Sprite spr : sprites) {

            if(coord.x >= spr.getX() && coord.y >= spr.getY() && coord.x <= spr.getX() + spr.getWidth() && coord.y <= spr.getY() + spr.getHeight()) {

                if(!clicked) {

                    clicked = true;

                }

            }

        }

        return clicked;

    }

    public static float cosInterpolate(float a, float b, float blend) {

        double ft = blend * 3.141592653589793D;
        float f = (float)((1.0D - Math.cos(ft)) * 0.5D);
        return a * (1.0F - f) + b * f;

    }

    public static float getAngle(Vector2 base, Vector2 target) {

        float angle = (float) Math.toDegrees(Math.atan2(target.y - base.y, target.x - base.x));

        if (angle < 0.0F) {

            angle += 360.0F;

        }

        return angle;

    }

    public static boolean isDifferentEnough(float a, float b, float c, float d, float e) {

        if(Math.abs(a - b) > 0.02F) { return true; } if(Math.abs(a - c) > 0.02F) { return true; } if(Math.abs(a - d) > 0.02F) { return true; } if(Math.abs(a - e) > 0.02F) { return true; }

        return false;

    }

    public static boolean isFull(int id, int x) {

        return false;

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

        while(turned && y<x) {

            y+=360;

        }

        while(!turned && y>x) {

            y-=360;

        }

        if(Math.abs(x - y) < 0.3f) return y;

        float result;

        float t = (y - x) * speed * Main.delta;
        if(t < -40 * speed * Main.delta) {

            t = -40 * speed * Main.delta;

        } else if(t > 40 * speed * Main.delta) {

            t = 40 * speed * Main.delta;

        }

        result = x + t;

        while(result > 360) result -= 360;
        while(result < 0) result += 360;

        return result;

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

        while(angle<0)angle+=360;
        while(angle>360)angle-=360;

        return angle;

    }

    /*public static boolean collideAABB(Hitbox box1, Hitbox box2) {

        if(box1.getLocation().x < box2.getLocation().x + box2.getWidth() && box1.getLocation().x + box1.getWidth() > box2.getLocation().x && box1.getLocation().y < box2.getLocation().y + box2.getHeight() && box1.getLocation().y + box1.getHeight() > box2.getLocation().y) {

            return true;

        }

        return false;

    }*/

    public static void fixBleeding(TextureRegion region) {

        float x = region.getRegionX(), y = region.getRegionY(), width = region.getRegionWidth(), height = region.getRegionHeight(), invTexWidth = 1f / region.getTexture().getWidth(), invTexHeight = 1f / region.getTexture().getHeight();
        region.setRegion((x + .5f) * invTexWidth, (y+.5f) * invTexHeight, (x + width - .5f) * invTexWidth, (y + height - .5f) * invTexHeight);

    }

}
