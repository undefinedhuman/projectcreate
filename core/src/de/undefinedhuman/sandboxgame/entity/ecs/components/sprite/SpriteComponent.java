package de.undefinedhuman.sandboxgame.entity.ecs.components.sprite;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

import java.util.Collection;
import java.util.HashMap;

public class SpriteComponent extends Component {

    private HashMap<String, SpriteData> spriteData;

    public SpriteComponent(Entity entity, HashMap<String, SpriteParam> params) {

        super(entity);

        this.spriteData = new HashMap<>();
        for (String name : params.keySet()) {

            SpriteParam param = params.get(name);
            SpriteData data = new SpriteData(entity, param.textureName);
            data.setRenderLevel(param.renderLevel);
            if (param.hasRegion) data.setHasRegion(param.region);
            spriteData.put(name, data);

        }
        this.type = ComponentType.SPRITE;

    }

    public String getTexture(String key) {
        return spriteData.get(key).getTexture();
    }

    public SpriteData getSpriteData(String key) {
        return spriteData.get(key);
    }

    public void setSpriteData(String name, SpriteData data) {
        this.spriteData.put(name, data);
    }

    public Collection<SpriteData> getSpriteData() {
        return spriteData.values();
    }

    @Override
    public void setNetworkData(LineSplitter s) {}

    @Override
    public void getNetworkData(LineWriter w) {

    }

}
