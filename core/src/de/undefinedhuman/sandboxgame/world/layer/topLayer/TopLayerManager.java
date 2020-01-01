package de.undefinedhuman.sandboxgame.world.layer.topLayer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.world.World;

import java.util.HashMap;

public class TopLayerManager {

    public static TopLayerManager instance;

    private HashMap<TopLayerType, TopLayerTexture[]> topLayers;

    public TopLayerManager() {
        topLayers = new HashMap<>();
    }

    public void load() {
        this.topLayers.put(TopLayerType.GRASS, loadTopLayer(TopLayerType.GRASS));
    }

    private TopLayerTexture[] loadTopLayer(TopLayerType type) {
        TopLayerTexture edgeDouble = new TopLayerTexture(new String[]{"world/TopLayers/" + type.toString() + "/Edge_Double.png"}, new Vector2(-2, 5), false, false);
        TopLayerTexture edgeLeft = new TopLayerTexture(new String[]{"world/TopLayers/" + type.toString() + "/Edge_Left.png"}, new Vector2(-2, 5), false, true);
        TopLayerTexture edgeRight = new TopLayerTexture(new String[]{"world/TopLayers/" + type.toString() + "/Edge_Right.png"}, new Vector2(0, 5), true, false);
        TopLayerTexture middle = new TopLayerTexture(new String[]{"world/TopLayers/" + type.toString() + "/Middle_0.png", "world/TopLayers/" + type.toString() + "/Middle_1.png", "world/TopLayers/" + type.toString() + "/Middle_2.png"}, new Vector2(0, 5), true, true);
        return new TopLayerTexture[] { middle, edgeLeft, edgeRight, edgeDouble };
    }

    public void render(SpriteBatch batch, int i, int j, TopLayerType type, boolean left, boolean right) {
        TopLayerTexture[] topLayerTextures = topLayers.get(type);
        for(TopLayerTexture texture : topLayerTextures)
            if(texture.left == left && texture.right == right) {
                World.instance.random.setSeed(World.instance.seed + i);
                batch.draw(TextureManager.instance.getTexture(texture.textures[World.instance.random.nextInt(texture.textures.length)]),i * 16 + texture.offset.x,j * 16 + texture.offset.y);
            }
    }

    public void delete() {
        for(TopLayerTexture[] topLayers : topLayers.values()) for(TopLayerTexture topLayerTexture : topLayers) topLayerTexture.delete();
    }

}
