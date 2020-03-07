package de.undefinedhuman.sandboxgame.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.utils.Tools;
import de.undefinedhuman.sandboxgame.utils.Variables;

public class BackgroundLayer {

    private float currentX, time = 0, blendFactor, localTime = 0;
    private Vector2 initialSize, size;
    private int currentTime = 1, nextTime = 2, transition = Variables.HOUR_LENGTH / 2;

    public BackgroundLayer(Vector2 size) {
        this.initialSize = size;
        currentX = -size.x;
    }

    public void resize(int width, int height) {
        setSize(height);
    }

    private void setSize(int height) {
        int scale = (int) Math.ceil(height / initialSize.y);
        this.size = new Vector2(initialSize.x * scale, initialSize.y * scale);
    }

    public void update(float delta, float speed) {

        time += delta;
        localTime += delta;
        localTime %= Variables.HOUR_LENGTH * 12;

        int currentDuration = Time.valueOf(currentTime).duration;

        if (time <= currentDuration - transition) blendFactor = 0;
        else {
            if (time >= currentDuration) {
                currentTime = nextTime;
                nextTime = (nextTime + 1) % Time.values().length;
                time = 0;
            }
            blendFactor = (time % transition) / transition;
        }

        if (speed == 0) return;
        currentX += speed * delta;
        currentX = Tools.clamp(currentX, -size.x, 0);
        currentX = currentX >= 0 ? -size.x : currentX <= -size.x ? 0 : currentX;

    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        float renderX = currentX;
        do {
            Color batchColor = batch.getColor();
            renderLayer(batch, camera, currentTime, renderX, 1 - blendFactor);
            renderLayer(batch, camera, nextTime, renderX, blendFactor);
            batch.setColor(batchColor);
            renderX += size.x;
        }while (renderX <= Gdx.graphics.getWidth());
    }

    private void renderLayer(SpriteBatch batch, OrthographicCamera camera, int id, float renderX, float blendFactor) {
        batch.setColor(1, 1, 1, blendFactor);
        batch.draw(TextureManager.instance.getTexture(Time.valueOf(id).texture), (camera != null ? camera.position.x - camera.viewportWidth / 2 : 0) + renderX, 0, size.x, size.y);
    }

    public int getTime() {
        return (int) localTime;
    }

}
