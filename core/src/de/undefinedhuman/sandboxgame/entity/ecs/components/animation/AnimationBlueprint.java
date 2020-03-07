package de.undefinedhuman.sandboxgame.entity.ecs.components.animation;

import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

import java.util.HashMap;

public class AnimationBlueprint extends ComponentBlueprint {

    private String defaultAnimation;
    private HashMap<String, AnimationParam> animations;

    public AnimationBlueprint() {
        this.defaultAnimation = "";
        this.animations = new HashMap<>();
        this.type = ComponentType.ANIMATION;
    }

    @Override
    public Component createInstance(Entity entity, HashMap<ComponentType, ComponentParam> params) {
        return new AnimationComponent(entity, defaultAnimation, animations);
    }

    @Override
    public void loadComponent(FileReader reader, int id) {
        reader.nextLine();
        this.defaultAnimation = reader.getNextString();
        int size = reader.getNextInt();
        for (int i = 0; i < size; i++) animations.put(reader.getNextString(), AnimationParam.load(reader));
    }

    @Override
    public void load(HashMap<String, LineSplitter> settings, int id) { }

    @Override
    public void delete() {
        animations.clear();
    }

}
