package de.undefinedhuman.projectcreate.game.background.birds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.base.GameObject;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.game.background.BackgroundManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;
import de.undefinedhuman.projectcreate.game.world.World;

public class Bird extends GameObject {

    private int currentIndex;
    private float animationTime = 0f, speedMultiplier;

    public Bird(Vector2 startPos, float speedMultiplier) {
        this.position.set(startPos);
        this.speedMultiplier = speedMultiplier;
        currentIndex = Tools.random.nextInt(BackgroundManager.getInstance().birdTexture.length);
    }

    @Override
    public void resize(int width, int height) {
        size.set(Variables.BIRD_SIZE).scl(BackgroundManager.getInstance().scale).scl(speedMultiplier);
    }

    @Override
    public void update(float delta) {
        animationTime += delta;
        if(animationTime >= Variables.BIRD_ANIMATION_SPEED) {
            currentIndex = Tools.loop(++currentIndex, 10);
            animationTime = 0;
        }
        position.x += Variables.BIRD_SPEED * speedMultiplier * delta;
        position.x += BackgroundManager.getInstance().speed * speedMultiplier * delta;
        position.x = position.x < -size.x ? World.instance.pixelSize.x : position.x > World.instance.pixelSize.x ? -size.x : position.x;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if (!Tools.isInRange(position.x, -size.x, camera.viewportWidth + size.x)) return;
        batch.draw(BackgroundManager.getInstance().birdTexture[currentIndex], (int) ((camera.position.x - camera.viewportWidth * 0.5f) + position.x), position.y, size.x, size.y);
    }

}
