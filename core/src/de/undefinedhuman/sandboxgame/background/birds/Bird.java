package de.undefinedhuman.sandboxgame.background.birds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.background.BackgroundManager;
import de.undefinedhuman.sandboxgame.engine.base.GameObject;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.utils.Tools;
import de.undefinedhuman.sandboxgame.world.World;

public class Bird extends GameObject {

    private int currentIndex;
    private float animationTime = 0f, speedMultiplier;

    public Bird(Vector2 startPos, float speedMultiplier) {
        this.position.set(startPos);
        this.speedMultiplier = speedMultiplier;
        currentIndex = Tools.random.nextInt(BackgroundManager.instance.birdTexture[0].length);
    }

    @Override
    public void init() {}

    @Override
    public void resize(int width, int height) {
        size.set(Variables.BIRD_WIDTH * BackgroundManager.instance.scale, Variables.BIRD_HEIGHT * BackgroundManager.instance.scale);
    }

    @Override
    public void update(float delta) {
        animationTime += delta;
        if(animationTime >= Variables.BIRD_ANIMATION_SPEED) {
            currentIndex = Tools.loop(++currentIndex, 10);
            animationTime = 0;
        }
        position.x += (Variables.BIRD_SPEED + BackgroundManager.instance.speed) * speedMultiplier * delta * BackgroundManager.instance.scale;
        position.x = position.x < -size.x ? World.instance.blockWidth : position.x > World.instance.blockWidth ? -size.x : position.x;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if(!Tools.isInRange(position.x, -size.x, camera.viewportWidth+size.x)) return;
        batch.draw(BackgroundManager.instance.birdTexture[0][currentIndex], (int) ((camera.position.x - camera.viewportWidth * 0.5f) + position.x), position.y, size.x, size.y);
    }

    @Override
    public void renderUI(SpriteBatch batch, OrthographicCamera camera) {}

    @Override
    public void delete() {}

}
