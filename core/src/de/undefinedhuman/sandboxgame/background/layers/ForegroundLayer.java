package de.undefinedhuman.sandboxgame.background.layers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.background.BackgroundManager;
import de.undefinedhuman.sandboxgame.background.Layer;
import de.undefinedhuman.sandboxgame.engine.resources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.world.World;

public class ForegroundLayer extends Layer {

    private float currentX, speedFactor, yOffset;
    private Vector2 initialSize, size;

    private String texture;

    public ForegroundLayer(String texture, Vector2 size, float speedFactor, float yOffset) {
        this.initialSize = size;
        this.texture = texture;
        this.speedFactor = speedFactor;
        this.yOffset = yOffset;
        currentX = -size.x;
    }

    @Override
    public void init() {
        TextureManager.instance.addTexture(texture);
    }

    @Override
    public void resize(int width, int height) {
        this.size = new Vector2(initialSize.x * BackgroundManager.instance.scale, initialSize.y * BackgroundManager.instance.scale);
    }

    @Override
    public void update(float delta, float speed) {
        if (speed == 0) return;
        currentX += (speed * speedFactor) * delta * BackgroundManager.instance.scale;
        currentX = currentX >= 0 ? -size.x : currentX <= -size.x ? 0 : currentX;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if(camera == null) return;
        Color batchColor = batch.getColor();
        float renderX = currentX;
        do {
            batch.draw(TextureManager.instance.getTexture(texture), (camera.position.x - camera.viewportWidth * 0.5f) + renderX, World.instance.maxHeight + yOffset * BackgroundManager.instance.scale, size.x, size.y);
            renderX += size.x;
        } while (renderX <= camera.viewportWidth);
        batch.setColor(batchColor);
    }

    @Override
    public void delete() {
        TextureManager.instance.removeTexture(texture);
    }

}
