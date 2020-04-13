package de.undefinedhuman.sandboxgame.engine.entity.components.sprite;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;

import java.util.Collection;
import java.util.HashMap;

public class SpriteComponent extends Component {

    private HashMap<String, SpriteData> spriteData;

    public SpriteComponent(Vector2 animationBounds, HashMap<String, SpriteLayer> params) {

        this.spriteData = new HashMap<>();
        for (String name : params.keySet()) {

            SpriteLayer param = params.get(name);
            SpriteData data = new SpriteData(param.texture.getString());
            data.setAnimated(param.isAnimated.getBoolean());
            data.setRenderLevel(param.renderLevel.getInt());
            if (animationBounds.x != 0 && animationBounds.y != 0) data.setAnimationBounds(animationBounds);
            spriteData.put(name, data);

        }
        this.type = ComponentType.SPRITE;

    }

    public String getTexture(String key) {
        return spriteData.get(key).getTexture();
    }

    public SpriteData getSpriteDataValue(String key) {
        return spriteData.get(key);
    }

    public void setSpriteData(String name, SpriteData data) {
        this.spriteData.put(name, data);
    }

    public Collection<SpriteData> getSpriteDataValue() {
        return spriteData.values();
    }

    public HashMap<String, SpriteData> getSpriteData() {
        return spriteData;
    }

    @Override
    public void receive(LineSplitter splitter) {}

    @Override
    public void send(LineWriter writer) {}

}
