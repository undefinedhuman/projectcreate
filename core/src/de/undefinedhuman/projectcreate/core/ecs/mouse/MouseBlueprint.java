package de.undefinedhuman.projectcreate.core.ecs.mouse;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;

public class MouseBlueprint extends ComponentBlueprint {
    @Override
    public Component createInstance() {
        return new MouseComponent();
    }
}
