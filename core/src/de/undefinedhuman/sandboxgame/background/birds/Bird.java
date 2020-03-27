package de.undefinedhuman.sandboxgame.background.birds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.background.BackgroundManager;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.utils.Tools;
import de.undefinedhuman.sandboxgame.world.World;

public class Bird {

    private int currentIndex = 0;
    private Vector2 position = new Vector2(), size = new Vector2();
    private float animationTime = 0f;

    public Bird(Vector2 startPos) {
        this.position.set(startPos);
    }

    public void resize(int width, int height) {
        size.set(Variables.BIRD_WIDTH * BackgroundManager.instance.scale, Variables.BIRD_HEIGHT * BackgroundManager.instance.scale);
    }

    public void update(float delta, float speed) {
        animationTime += delta;
        if(animationTime >= Variables.BIRD_ANIMATION_SPEED) {
            currentIndex = Tools.loop(currentIndex, 10);
            animationTime = 0;
        }
        position.x += (Variables.BIRD_SPEED + speed) * delta * BackgroundManager.instance.scale;
        position.x = position.x < -size.x ? World.instance.blockWidth : position.x > World.instance.blockWidth ? -size.x : position.x;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.draw(BackgroundManager.instance.birdTexture[0][currentIndex], (int) ((camera.position.x - camera.viewportWidth * 0.5f) + position.x), position.y, size.x, size.y);
    }

}
