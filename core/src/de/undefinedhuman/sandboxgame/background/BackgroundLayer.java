package de.undefinedhuman.sandboxgame.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;

public class BackgroundLayer extends Layer {

    private float currentX, localTime = 0, blendFactor;
    private Vector2 initialSize, size;
    private int currentTime = 1, nextTime = 2;

    public BackgroundLayer(Vector2 size) {
        this.initialSize = size;
        currentX = -size.x;
    }

    @Override
    public void init() {}

    @Override
    public void resize(int width, int height) {
        float ratio = ((float) height / (float) width) * 1280f / initialSize.y;
        this.size = new Vector2(initialSize.x * ratio, initialSize.y * ratio);
    }

    @Override
    public void update(float delta, float speed) {

        localTime += delta;

        int currentDuration = Time.valueOf(currentTime).duration;
        int transition = Variables.HOUR_LENGTH / 2;

        if (localTime <= currentDuration - transition) blendFactor = 0;
        else {
            if (localTime >= currentDuration) {
                currentTime %= (currentTime + 1) & Time.values().length;
                localTime = 0;
            }
            blendFactor = (localTime % transition) / transition;
        }

        if (speed == 0) return;
        currentX -= speed * delta;
        currentX = currentX >= 0 ? -size.x : currentX <= -size.x ? 0 : currentX;

    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        Color batchColor = batch.getColor();
        float renderX = currentX;
        do {
            // TODO Create actual transition images for the other day times
            // renderLayer(batch, camera, currentTime, renderX, 1 - blendFactor);
            // renderLayer(batch, camera, (currentTime + 1) % Time.values().length, renderX, blendFactor);
            renderLayer(batch, camera, 0, renderX, 1);
            renderX += size.x;
        } while (renderX <= camera.viewportWidth);
        batch.setColor(batchColor);
    }

    @Override
    public void delete() {}

    private void renderLayer(SpriteBatch batch, OrthographicCamera camera, int id, float renderX, float blendFactor) {
        batch.setColor(1, 1, 1, blendFactor);
        if(camera != null) batch.draw(TextureManager.instance.getTexture(Time.valueOf(id).texture), (camera.position.x - camera.viewportWidth * 0.5f) + renderX, (camera.position.y - camera.viewportHeight * 0.5f), size.x, size.y);
        else batch.draw(TextureManager.instance.getTexture(Time.valueOf(id).texture), 0 + renderX, 0, size.x, size.y);
    }

}
