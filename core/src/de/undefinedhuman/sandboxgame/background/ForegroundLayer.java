package de.undefinedhuman.sandboxgame.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.utils.Tools;
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
        currentX = Tools.clamp(currentX + (speed * speedFactor) * delta * BackgroundManager.instance.scale, -size.x, 0);
        currentX = currentX >= 0 ? -size.x : currentX <= -size.x ? 0 : currentX;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        Color batchColor = batch.getColor();
        float renderX = currentX;
        do {
            batch.draw(TextureManager.instance.getTexture(texture), (camera != null ? camera.position.x - camera.viewportWidth * 0.5f : 0) + renderX, World.instance.maxHeight + (yOffset * BackgroundManager.instance.scale), size.x, size.y);
            renderX += size.x;
        } while (renderX <= (camera != null ? camera.viewportWidth : Gdx.graphics.getWidth()));
        batch.setColor(batchColor);
    }

    @Override
    public void delete() {
        TextureManager.instance.removeTexture(texture);
    }

}
