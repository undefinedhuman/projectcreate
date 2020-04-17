package de.undefinedhuman.sandboxgame.background.clouds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.background.BackgroundManager;
import de.undefinedhuman.sandboxgame.engine.base.GameObject;
import de.undefinedhuman.sandboxgame.engine.resources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.utils.Tools;
import de.undefinedhuman.sandboxgame.world.World;

public class Cloud extends GameObject {

    public float alpha = 1f;

    private String texture;
    private float speedMultiplier, baseSpeed, alphaMultiplier = 0f;

    public Cloud(String texture, Vector2 startPos, float speedMultiplier) {
        this.texture = texture;
        this.speedMultiplier = speedMultiplier;
        this.baseSpeed = Variables.CLOUD_BASE_SPEED * (Tools.random.nextInt(8) + 1);
        this.position.set(startPos);
    }

    @Override
    public void init() {}

    @Override
    public void resize(int width, int height) {
        TextureRegion region = TextureManager.instance.getTexture(texture);
        size.set(region.getRegionWidth() * BackgroundManager.instance.scale, region.getRegionHeight() * BackgroundManager.instance.scale);
    }

    @Override
    public void update(float delta) {
        this.alpha -= alphaMultiplier * delta;
        position.x += (-baseSpeed + BackgroundManager.instance.speed) * speedMultiplier * delta * BackgroundManager.instance.scale;
        position.x = position.x < -size.x ? World.instance.blockWidth : position.x > World.instance.blockWidth ? -size.x : position.x;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if(!Tools.isInRange(position.x, -size.x, camera.viewportWidth+size.x)) return;
        Color batchColor = batch.getColor();
        batch.setColor(1, 1, 1, alpha);
        TextureRegion region = Tools.fixBleeding(TextureManager.instance.getTexture(texture));
        batch.draw(region, (camera.position.x - camera.viewportWidth * 0.5f) + position.x, position.y, size.x, size.y);
        batch.setColor(batchColor);
    }

    @Override
    public void renderUI(SpriteBatch batch, OrthographicCamera camera) {}

    @Override
    public void delete() {}

    public void setAlphaMultiplier(float alphaMultiplier) {
        this.alphaMultiplier = alphaMultiplier;
    }

}
