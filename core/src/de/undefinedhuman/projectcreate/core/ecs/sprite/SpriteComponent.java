package de.undefinedhuman.projectcreate.core.ecs.sprite;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.engine.base.Transform;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

public class SpriteComponent implements Component {

    private HashMap<String, SpriteData> spriteData;
    private SpriteData[] sortedSpriteData;

    public SpriteComponent(HashMap<String, SpriteLayer> spriteLayers) {
        this.spriteData = new HashMap<>();
        spriteLayers.forEach((key, spriteLayer) -> spriteData.put(key, spriteLayer.createSpriteData()));
    }

    public void init() {
        sortedSpriteData = spriteData.values().stream().sorted(Comparator.comparingInt(SpriteData::getRenderLevel)).toArray(SpriteData[]::new);
    }

    public void render(SpriteBatch batch, Transform transform, int renderOffset) {
        if(sortedSpriteData == null)
            return;
        Arrays.stream(sortedSpriteData).forEach(spriteData -> spriteData.render(batch, transform, renderOffset));
    }

    public void delete() {
        sortedSpriteData = null;
        for(SpriteData data : spriteData.values())
            data.delete();
        spriteData.clear();
    }

    public Collection<SpriteData> getSpriteData() {
        return spriteData.values();
    }
    public SpriteData getSpriteData(String key) {
        return spriteData.get(key);
    }

    public void setFrameIndex(int animationIndex) {
        for(SpriteData data : spriteData.values())
            data.setFrameIndex(animationIndex);
    }

}
