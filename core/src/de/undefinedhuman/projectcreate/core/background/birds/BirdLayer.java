package de.undefinedhuman.projectcreate.core.background.birds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.background.Layer;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.core.utils.Tools;
import de.undefinedhuman.projectcreate.core.world.World;

import java.util.ArrayList;

public class BirdLayer extends Layer {

    private ArrayList<Bird> birds = new ArrayList<>();
    private Color color = new Color();
    private int yOffset;
    private float speedMultiplier;

    public BirdLayer(Color color, int yOffset, float speedMultiplier) {
        this.color.set(color);
        this.yOffset = yOffset;
        this.speedMultiplier = speedMultiplier;
    }

    @Override
    public void init() {
        int birdCount = Tools.calculateRandomValue((World.instance.pixelSize.x/(World.instance.size.x*2))/3), xOffset = Tools.calculateRandomValue(World.instance.pixelSize.x/birdCount/2);
        for(int i = 0; i < birdCount; i++) {
            int groupCount = Tools.random.nextInt(3) + 3;
            for(int j = 0; j < groupCount; j++)
                birds.add(new Bird(new Vector2(i * xOffset + (int) (j * Variables.BIRD_SIZE.x * 1.5f), World.instance.maxHeight + yOffset + Tools.random.nextInt(Variables.BIRD_HEIGHT_OFFSET * 2) - Variables.BIRD_HEIGHT_OFFSET), speedMultiplier));
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
