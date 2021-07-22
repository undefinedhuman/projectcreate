package de.undefinedhuman.projectcreate.core.ecs.angle;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;

public class AngleBlueprint extends ComponentBlueprint {

    @Override
    public Component createInstance() {
        return new AngleComponent();
    }

}
