package de.undefinedhuman.sandboxgame.engine.entity.components.animation;

import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentParam;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.panels.Panel;

import java.util.HashMap;

public class AnimationBlueprint extends ComponentBlueprint {

    private Setting defaultAnimation = new Setting(SettingType.String, "Default Animation", "Idle");
    private Panel animations = new Panel(SettingType.Animation, "Animations", new Animation());

    public AnimationBlueprint() {
        settings.addSettings(defaultAnimation, animations);
        this.type = ComponentType.ANIMATION;
    }

    @Override
    public void load(FileReader reader) {
        super.load(reader);
        animations.loadObjects(reader);
    }

    @Override
    public void save(FileWriter writer) {
        super.save(writer);
        animations.saveObjects(writer);
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        HashMap<String, Animation> animationList = new HashMap<>();
        for(String key : animations.getObjects().keySet()) animationList.put(key, (Animation) animations.getObjects().get(key));
        return new AnimationComponent(defaultAnimation.getString(), animationList);
    }

    @Override
    public void delete() {}

}
