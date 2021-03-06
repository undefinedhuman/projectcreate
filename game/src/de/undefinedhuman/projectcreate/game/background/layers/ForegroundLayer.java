package de.undefinedhuman.projectcreate.game.background.layers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.game.background.BackgroundManager;
import de.undefinedhuman.projectcreate.game.background.Layer;
import de.undefinedhuman.projectcreate.game.world.World;

public class ForegroundLayer extends Layer {

    private float currentX, speedFactor, yOffset, brightness;
    private Vector2 initialSize, size = new Vector2();

    private String texture;

    public ForegroundLayer(String texture, Vector2 size, float speedFactor, float yOffset, float brightness) {
        this.initialSize = size;
        this.texture = texture;
        this.speedFactor = speedFactor;
        this.yOffset = yOffset;
        this.brightness = brightness;
        currentX = -size.x;
    }

    @Override
    public void init() {
        TextureManager.getInstance().loadTextures(texture);
    }

    @Override
    public void resize(int width, int height) {
        this.size.set(initialSize).scl(BackgroundManager.getInstance().scale);
    }

    @Override
    public void update(float delta, float speed) {
        if (speed == 0) return;
        currentX += (speed * speedFactor) * delta;
        currentX = currentX >= 0 ? -size.x : currentX <= -size.x ? 0 : currentX;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if(camera == null) return;
        Color batchColor = batch.getColor();
        batch.setColor(brightness, brightness, brightness, 75f);
        float renderX = currentX;
        do {
            batch.draw(TextureManager.getInstance().getTexture(texture), (camera.position.x - camera.viewportWidth * 0.5f) + renderX, World.instance.maxHeight + yOffset * BackgroundManager.getInstance().scale, size.x, size.y);
            renderX += size.x;
        } while (renderX <= camera.viewportWidth);
        batch.setColor(batchColor);
    }

    @Override
    public void delete() {
        TextureManager.getInstance().removeTextures(texture);
    }

}
