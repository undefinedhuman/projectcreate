package de.undefinedhuman.sandboxgame.entity.ecs.components.sprite;

import com.badlogic.gdx.math.Vector2;

public class SpriteParam {

    public String textureName;
    public int renderLevel;
    public Vector2 region;
    public boolean hasRegion;

    public SpriteParam(String textureName, int renderLevel, Vector2 region) {

        this.textureName = textureName;
        this.renderLevel = renderLevel;
        this.hasRegion = (region.x != 0 && region.y != 0);
        this.region = region;

    }
    
}
