package de.undefinedhuman.sandboxgame.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.world.World;

import java.util.ArrayList;
import java.util.Random;

public class CloudLayer extends Layer {

    private ArrayList<Cloud> clouds = new ArrayList<>();

    private int yOffset;
    private Random random = new Random();

    public CloudLayer(int yOffset) {
        this.yOffset = yOffset;
        this.random.setSeed(World.instance.seed + yOffset * 100);
    }

    @Override
    public void init() {
        for(int i = 0; i < 100; i++) {
            clouds.add(new Cloud(
                    new Vector2(random.nextInt(Gdx.graphics.getBackBufferWidth()), World.instance.maxHeight + yOffset + random.nextInt(60) - 30),
                    5 + random.nextInt(10),
                    0));
        }
    }

    @Override
    public void resize(int width, int height) {
        for(Cloud cloud : clouds) cloud.resize(width, height);
    }

    public void update(float delta, float speed) {
        ArrayList<Cloud> removeClouds = new ArrayList<>();
        for(Cloud cloud : clouds) {
            cloud.update(delta, speed);
            if(cloud.dead) removeClouds.add(cloud);
        }
        clouds.removeAll(removeClouds);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        for(Cloud cloud : clouds) cloud.render(batch, camera);
    }

    @Override
    public void delete() {}

}
