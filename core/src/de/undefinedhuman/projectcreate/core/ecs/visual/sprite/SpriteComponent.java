package de.undefinedhuman.projectcreate.core.ecs.visual.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import de.undefinedhuman.projectcreate.engine.base.Transform;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.ds.ImmutableArray;

import java.util.HashMap;

public class SpriteComponent implements Component {

    private HashMap<String, SpriteData> spriteData = new HashMap<>();
    private Array<SpriteData> sortedSpriteData = new Array<>(true, 16);
    private ImmutableArray<SpriteData> immutableSortedSpriteData = new ImmutableArray<>(sortedSpriteData);
    private boolean sorted = false;

    public SpriteComponent(HashMap<String, SpriteLayer> spriteLayers) {
        spriteLayers.forEach((key, spriteLayer) -> addSpriteData(key, spriteLayer.createSpriteData()));
    }

    public void render(SpriteBatch batch, Transform transform, int renderOffset) {
        getSortedSpriteData().forEach(data -> data.render(batch, transform, renderOffset));
    }

    public void delete() {
        sortedSpriteData.clear();
        for(SpriteData data : spriteData.values())
            data.delete();
        spriteData.clear();
        sorted = true;
    }

    public ImmutableArray<SpriteData> getSortedSpriteData() {
        if(!sorted) {
            sortedSpriteData.sort();
            sorted = true;
        }
        return immutableSortedSpriteData;
    }

    public SpriteData getSpriteData(String key) {
        return spriteData.get(key);
    }

    public void setFrameIndex(int animationIndex) {
        spriteData.values().forEach(spriteData -> spriteData.setFrameIndex(animationIndex));
    }

    public void setTurned(boolean turned) {
        spriteData.values().forEach(spriteData -> spriteData.setTurned(turned));
    }

    private void addSpriteData(String key, SpriteData data) {
        if(spriteData.containsKey(key))
            Log.warn("SpriteComponent already contains sprite data with name: " + key);
        this.spriteData.put(key, data);
        this.sortedSpriteData.add(data);
        sorted = false;
    }

}
