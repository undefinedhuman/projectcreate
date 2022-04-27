package de.undefinedhuman.projectcreate.core.ecs.mouse;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;

public class MouseBlueprint extends ComponentBlueprint {
    @Override
    public Component createInstance() {
        return new MouseComponent();
    }
}
