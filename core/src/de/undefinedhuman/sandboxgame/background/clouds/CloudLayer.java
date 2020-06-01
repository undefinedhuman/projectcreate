package de.undefinedhuman.sandboxgame.background.clouds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.background.BackgroundManager;
import de.undefinedhuman.sandboxgame.background.Layer;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.utils.Tools;
import de.undefinedhuman.sandboxgame.world.World;

import java.util.ArrayList;

public class CloudLayer extends Layer {

    private ArrayList<Cloud> clouds = new ArrayList<>(), cloudsToRemove = new ArrayList<>();

    private int yOffset;
    private int layerID;

    public CloudLayer(int yOffset, int layerID) {
        this.yOffset = yOffset;
        this.layerID = layerID;
    }

    @Override
    public void init() {
        int cloudCount = World.instance.blockWidth/200;
        for(int i = 0; i < cloudCount; i++) {
            clouds.add(new Cloud(
                    BackgroundManager.instance.cloudTextures[Tools.random.nextInt(BackgroundManager.instance.cloudTextures.length)],
                    new Vector2(Tools.random.nextInt(World.instance.blockWidth), World.instance.maxHeight + yOffset + Tools.random.nextInt(Variables.CLOUD_HEIGHT_OFFSET * 2) - Variables.CLOUD_HEIGHT_OFFSET),
                    layerID));
        }
    }

    @Override
    public void resize(int width, int height) {
        for(Cloud cloud : clouds) cloud.resize(width, height);
    }

    public void update(float delta, float speed) {
        for(Cloud cloud : clouds) {
            cloud.update(delta);
            if(cloud.alpha <= 0f) cloudsToRemove.add(cloud);
        }
        clouds.removeAll(cloudsToRemove);
        cloudsToRemove.clear();
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        for(Cloud cloud : clouds) cloud.render(batch, camera);
    }

    @Override
    public void delete() {
        clouds.clear();
    }

}
