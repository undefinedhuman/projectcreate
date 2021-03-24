package de.undefinedhuman.sandboxgame.engine.entity.components.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;

import java.util.*;

public class SpriteComponent extends Component {

    private HashMap<String, SpriteData> spriteData;
    private SortedMap<Integer, ArrayList<SpriteData>> orderedSpriteData = new TreeMap<>();

    public SpriteComponent(int animationBounds, HashMap<String, SpriteLayer> params) {
        this.spriteData = new HashMap<>();
        for (String name : params.keySet()) {
            SpriteLayer layer = params.get(name);
            SpriteData data = new SpriteData(layer.texture.getString(), layer.isAnimated.getBoolean() ? animationBounds : 1, layer.renderLevel.getInt());
            spriteData.put(name, data);
        }
        this.type = ComponentType.SPRITE;
    }

    @Override
    public void init() {
        for(SpriteData data : spriteData.values()) {
            int renderLevel = data.getRenderLevel();
            if (orderedSpriteData.containsKey(renderLevel)) orderedSpriteData.get(renderLevel).add(data);
            else {
                ArrayList<SpriteData> spriteData = new ArrayList<>();
                spriteData.add(data);
                orderedSpriteData.put(renderLevel, spriteData);
            }
        }
    }

    public Collection<SpriteData> getSpriteData() {
        return spriteData.values();
    }
    public SpriteData getSpriteData(String key) {
        return spriteData.get(key);
    }

    public void render(SpriteBatch batch, int renderOffset) {
        for(int renderLevel : orderedSpriteData.keySet()) {
            ArrayList<SpriteData> spriteData = orderedSpriteData.get(renderLevel);
            for(SpriteData data : spriteData) data.render(batch, renderOffset);
        }
    }

    @Override
    public void delete() {
        orderedSpriteData.clear();
        for(SpriteData data : spriteData.values()) data.delete();
        spriteData.clear();
    }

    @Override
    public void receive(LineSplitter splitter) {}

    @Override
    public void send(LineWriter writer) {}

}
