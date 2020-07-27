package de.undefinedhuman.sandboxgame.engine.entity.components.animation;

import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentParam;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.panels.Panel;
import de.undefinedhuman.sandboxgame.engine.settings.panels.StringPanel;

import java.util.HashMap;

public class AnimationBlueprint extends ComponentBlueprint {

    private Setting defaultAnimation = new Setting(SettingType.String, "Default Animation", "Idle");
    private Panel animations = new StringPanel("Animation", new Animation());

    public AnimationBlueprint() {
        settings.addSettings(defaultAnimation, animations);
        this.type = ComponentType.ANIMATION;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        HashMap<String, Animation> animationList = new HashMap<>();
        for(String key : animations.getPanelObjects().keySet()) animationList.put(key, (Animation) animations.getPanelObjects().get(key));
        return new AnimationComponent(defaultAnimation.getString(), animationList);
    }

    @Override
    public void delete() {}

}
