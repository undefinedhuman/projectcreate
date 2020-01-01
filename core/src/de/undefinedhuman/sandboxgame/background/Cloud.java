package de.undefinedhuman.sandboxgame.background;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;

public class Cloud {

    public boolean dead = false;
    private Sprite sprite;
    private float startY, speed;
    private float x;

    public Cloud(String textureName, int startX, int startY, int width, int height, int speed) {

        this.sprite = new Sprite(TextureManager.instance.getTexture(textureName));
        this.sprite.setPosition(startX, startY);
        this.x = startX;
        this.startY = startY;
        this.speed = speed;
        this.sprite.setSize(width, height);

    }

    public void update(float delta) {

        sprite.setPosition(sprite.getX() - (30 + speed) * delta, startY);
        if (sprite.getX() + sprite.getWidth() <= 0) dead = true;

    }

    public void update(float delta, OrthographicCamera camera) {
        x = x - (30 + speed) * delta;
        sprite.setPosition((camera.position.x - camera.viewportWidth / 2) + x, startY);
        if (sprite.getX() + sprite.getWidth() <= camera.position.x - camera.viewportWidth / 2) dead = true;
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

}
