package de.undefinedhuman.projectcreate.core.ecs.interaction;

import com.badlogic.gdx.Input;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

import java.util.HashMap;

public class InteractionBlueprint extends ComponentBlueprint {

    public IntSetting
            range = new IntSetting("Range", 0);

    public Setting<Integer>
            inputKey = new Setting<>("Input Key", "F", value -> Input.Keys.valueOf(String.valueOf(value)));

    public InteractionBlueprint() {
        super(InteractionComponent.class);
        settings.addSettings(range, inputKey);
    }

    @Override
    public Component createInstance(HashMap<Class<? extends Component>, ComponentParam> params) {
        return new InteractionComponent(range.getValue(), inputKey.getValue());
    }

}
