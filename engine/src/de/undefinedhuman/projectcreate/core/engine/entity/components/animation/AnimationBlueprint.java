package de.undefinedhuman.projectcreate.core.engine.entity.components.animation;

import de.undefinedhuman.projectcreate.core.engine.entity.Component;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.core.engine.settings.Setting;
import de.undefinedhuman.projectcreate.core.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.core.engine.settings.panels.Panel;
import de.undefinedhuman.projectcreate.core.engine.settings.panels.StringPanel;

import java.util.HashMap;

public class AnimationBlueprint extends ComponentBlueprint {

    private Setting defaultAnimation = new Setting(SettingType.String, "Default Animation", "Idle");
    private Panel<Animation> animations = new StringPanel<>("Animation", new Animation());

    public AnimationBlueprint() {
        settings.addSettings(defaultAnimation, animations);
        this.type = ComponentType.ANIMATION;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        return new AnimationComponent(defaultAnimation.getString(), animations.getPanelObjects());
    }

    @Override
    public void delete() {}

}
