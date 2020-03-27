package de.undefinedhuman.sandboxgame.background.clouds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.background.BackgroundManager;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.utils.Tools;
import de.undefinedhuman.sandboxgame.world.World;

public class Cloud {

    public float alpha = 1f;

    private String texture;
    private Vector2 position = new Vector2(), size = new Vector2();
    private float speedMultiplier, baseSpeed, alphaMultiplier = 0f;

    public Cloud(String texture, Vector2 startPos, float speedMultiplier) {
        this.texture = texture;
        this.position.set(startPos);
        this.speedMultiplier = speedMultiplier;
        this.baseSpeed = Variables.CLOUD_BASE_SPEED * (Tools.random.nextInt(8) + 1);
    }

    public void resize(int width, int height) {
        TextureRegion region = TextureManager.instance.getTexture(texture);
        size.set(region.getRegionWidth() * BackgroundManager.instance.scale, region.getRegionHeight() * BackgroundManager.instance.scale);
    }

    public void update(float delta, float speed) {
        this.alpha -= alphaMultiplier * delta;
        position.x += (-baseSpeed + speed) * speedMultiplier * delta * BackgroundManager.instance.scale;
        position.x = position.x < -size.x ? World.instance.blockWidth : position.x > World.instance.blockWidth ? -size.x : position.x;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        Color batchColor = batch.getColor();
        batch.setColor(1, 1, 1, alpha);
        batch.draw(TextureManager.instance.getTexture(texture), (int) ((camera.position.x - camera.viewportWidth * 0.5f) + position.x), position.y, size.x, size.y);
        batch.setColor(batchColor);
    }

    public void setAlphaMultiplier(float alphaMultiplier) {
        this.alphaMultiplier = alphaMultiplier;
    }

}
