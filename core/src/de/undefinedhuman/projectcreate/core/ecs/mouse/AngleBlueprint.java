package de.undefinedhuman.projectcreate.core.ecs.mouse;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;

import java.util.HashMap;

public class AngleBlueprint extends ComponentBlueprint {

    public AngleBlueprint() {
        super(AngleComponent.class);
    }

    @Override
    public Component createInstance(HashMap<Class<? extends Component>, ComponentParam> params) {
        return new AngleComponent();
    }

    @Override
    public void delete() {}

}
