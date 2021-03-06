package de.undefinedhuman.projectcreate.game.background.clouds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.game.background.BackgroundManager;
import de.undefinedhuman.projectcreate.game.background.Layer;
import de.undefinedhuman.projectcreate.game.utils.Tools;
import de.undefinedhuman.projectcreate.game.world.World;

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
        int cloudCount = World.instance.pixelSize.x/200;
        for(int i = 0; i < cloudCount; i++) {
            clouds.add(new Cloud(
                    BackgroundManager.getInstance().cloudTextures[Tools.RANDOM.nextInt(BackgroundManager.getInstance().cloudTextures.length)],
                    new Vector2(Tools.RANDOM.nextInt(World.instance.pixelSize.x), World.instance.maxHeight + yOffset + Tools.RANDOM.nextInt(Variables.CLOUD_HEIGHT_OFFSET * 2) - Variables.CLOUD_HEIGHT_OFFSET),
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
