package de.undefinedhuman.projectcreate.core.ecs.interaction;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Input;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentPriority;
import de.undefinedhuman.projectcreate.engine.settings.types.TextFieldSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class InteractionBlueprint extends ComponentBlueprint {

    public IntSetting
            range = new IntSetting("Range", 0);

    public TextFieldSetting<Integer>
            inputKey = new TextFieldSetting<>("Input Key", Input.Keys.F, Input.Keys::valueOf, Input.Keys::toString);

    public InteractionBlueprint() {
        addSettings(range, inputKey);
        priority = ComponentPriority.MEDIUM;
    }

    @Override
    public Component createInstance() {
        return new InteractionComponent(range.getValue(), inputKey.getValue());
    }

}
