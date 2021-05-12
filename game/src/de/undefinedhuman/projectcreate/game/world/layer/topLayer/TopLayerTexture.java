package de.undefinedhuman.projectcreate.game.world.layer.topLayer;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;

public class TopLayerTexture {

    public String[] textures;
    public Vector2 offset;
    public boolean left, right;

    public TopLayerTexture(String[] textures, Vector2 offset, boolean left, boolean right) {
        this.textures = textures;
        this.offset = offset;
        this.left = left;
        this.right = right;
        TextureManager.instance.addTextures(textures);
    }

    public void delete() {
        TextureManager.instance.removeTextures(textures);
    }

}
