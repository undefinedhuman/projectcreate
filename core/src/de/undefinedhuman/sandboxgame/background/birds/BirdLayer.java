package de.undefinedhuman.sandboxgame.background.birds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.background.Layer;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.utils.Tools;
import de.undefinedhuman.sandboxgame.world.World;

import java.util.ArrayList;

public class BirdLayer extends Layer {

    private Color color;
    private ArrayList<Bird> birds = new ArrayList<>();
    private int yOffset;
    private float speedMultiplier, brightness;

    public BirdLayer(Color color, int yOffset, float speedMultiplier, float brightness) {
        this.color = color;
        this.color.mul(brightness, brightness, brightness, 1f);
        this.yOffset = yOffset;
        this.speedMultiplier = speedMultiplier;
        this.brightness = brightness;
    }

    @Override
    public void init() {
        int birdCount = Tools.calculateRandomValue((World.instance.blockWidth/(World.instance.width*2))/3), xOffset = Tools.calculateRandomValue(World.instance.blockWidth/birdCount/2);
        for(int i = 0; i < birdCount; i++) {
            int groupCount = Tools.random.nextInt(3) + 3;
            for(int j = 0; j < groupCount; j++)
                birds.add(new Bird(new Vector2(i * xOffset + (int) (j * Variables.BIRD_WIDTH * 1.5f), World.instance.maxHeight + yOffset + Tools.random.nextInt(Variables.BIRD_HEIGHT_OFFSET * 2) - Variables.BIRD_HEIGHT_OFFSET), speedMultiplier));
        }
    }

    @Override
    public void resize(int width, int height) {
        for(Bird bird : birds) bird.resize(width, height);
    }

    public void update(float delta, float speed) {
        for(Bird bird : birds) bird.update(delta);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.setColor(color);
        for(Bird bird : birds) bird.render(batch, camera);
        batch.setColor(Color.WHITE);
    }

    @Override
    public void delete() {
        birds.clear();
    }

}
