package de.undefinedhuman.sandboxgame.background;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.base.GameObject;
import de.undefinedhuman.sandboxgame.utils.Tools;
import de.undefinedhuman.sandboxgame.world.World;

public class BackgroundObject extends GameObject {

    protected int layerID;

    private Vector2 baseSize = new Vector2();
    private float baseMultiplier, speed;

    public BackgroundObject(Vector2 baseSize, Vector2 startPos, float speed, int layerID) {
        this.position.set(startPos);
        this.baseSize.set(baseSize);
        this.speed = speed;
        this.baseMultiplier = 1f - layerID * 0.25f;
        this.layerID = layerID;
    }

    @Override
    public void resize(int width, int height) {
        size.set(baseSize).scl(BackgroundManager.instance.scale).scl(1f - layerID * 0.15f);
    }

    @Override
    public void update(float delta) {
        position.x += speed * baseMultiplier * delta;
        position.x += BackgroundManager.instance.speed * baseMultiplier * delta;
        position.x = position.x < -size.x ? World.instance.blockWidth : position.x > World.instance.blockWidth ? -size.x : position.x;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if (!Tools.isInRange(position.x, -size.x, camera.viewportWidth + size.x)) return;
    }

}
