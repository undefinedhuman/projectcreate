package de.undefinedhuman.sandboxgame.entity.ecs.components.sprite;

import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

import java.util.HashMap;

public class SpriteBlueprint extends ComponentBlueprint {

    private HashMap<String, SpriteParam> blueprints;

    public SpriteBlueprint() {
        this.type = ComponentType.SPRITE;
        this.blueprints = new HashMap<>();
    }

    @Override
    public Component createInstance(Entity entity, HashMap<ComponentType, ComponentParam> params) {
        return new SpriteComponent(entity, blueprints);
    }

    @Override
    public void loadComponent(FileReader reader, int id) {
        LineSplitter splitter = new LineSplitter(reader.nextLine(), true, ";");
        int size = splitter.getNextInt();
        String[] sprites = new String[size];
        for (int i = 0; i < size; i++) {
            String name = splitter.getNextString(), textureName = Paths.ENTITY_FOLDER.getPath() + id + "/" + splitter.getNextString();
            SpriteParam data = new SpriteParam(textureName, splitter.getNextInt(), splitter.getNextVector2());
            sprites[i] = textureName;
            blueprints.put(name, data);
        }
        TextureManager.instance.addTexture(sprites);
    }

    @Override
    public void load(HashMap<String, LineSplitter> settings, int id) {}

    @Override
    public void delete() {
        for (SpriteParam param : blueprints.values()) TextureManager.instance.removeTexture(param.textureName);
        blueprints.clear();
    }

}
