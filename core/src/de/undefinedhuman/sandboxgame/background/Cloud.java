package de.undefinedhuman.sandboxgame.background;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;

public class Cloud {

    private String texture;
    private Vector2 position = new Vector2();
    private Vector2 size = new Vector2();
    private float speed, speedFactor;

    public boolean dead = false;

    public Cloud(String texture, Vector2 startPos, int speed, float speedFactor) {
        this.texture = texture;
        this.position.set(startPos);
        this.speed = speed;
        this.speedFactor = speedFactor;
    }

    public void resize(int width, int height) {
        TextureRegion region = TextureManager.instance.getTexture(texture);
        size.set(region.getRegionWidth() * BackgroundManager.instance.scale, region.getRegionHeight() * BackgroundManager.instance.scale);
    }

    public void update(float delta, float speed) {
        position.x -= this.speed * delta;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.draw(TextureManager.instance.getTexture(texture), position.x, position.y, size.x, size.y);
        if (position.x + size.x <= camera.position.x - camera.viewportWidth * 0.5f) dead = true;
    }

}
