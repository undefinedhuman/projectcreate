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

    public BirdLayer(Color color, int yOffset) {
        this.color = color;
        this.yOffset = yOffset;
    }

    @Override
    public void init() {
        int birdCount = World.instance.blockWidth/(World.instance.width*2), xOffset = World.instance.blockWidth/birdCount;
        for(int i = 0; i < birdCount; i++) {
            int tempBirdCount = Tools.random.nextInt(2);
            for(int j = 0; j < tempBirdCount; j++)
                birds.add(new Bird(new Vector2(i * xOffset, World.instance.maxHeight + yOffset + Tools.random.nextInt(Variables.BIRD_HEIGHT_OFFSET * 2) - Variables.BIRD_HEIGHT_OFFSET)));
        }
    }

    @Override
    public void resize(int width, int height) {
        for(Bird bird : birds) bird.resize(width, height);
    }

    public void update(float delta, float speed) {
        for(Bird bird : birds) bird.update(delta, speed);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        Color batchColor = batch.getColor();
        batch.setColor(color);
        for(Bird bird : birds) bird.render(batch, camera);
        batch.setColor(batchColor);
    }

    @Override
    public void delete() {
        birds.clear();
    }

}
