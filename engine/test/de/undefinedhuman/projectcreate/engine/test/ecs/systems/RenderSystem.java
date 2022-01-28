package de.undefinedhuman.projectcreate.engine.test.ecs.systems;

import de.undefinedhuman.projectcreate.engine.ecs.System;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.All;
import de.undefinedhuman.projectcreate.engine.test.ecs.components.SpriteComponent;
import de.undefinedhuman.projectcreate.engine.test.ecs.components.TransformComponent;

import java.util.ArrayList;

@All({ TransformComponent.class, SpriteComponent.class })
public class RenderSystem extends System {

    private ArrayList<Long> processedEntities = new ArrayList<>();

    @Override
    protected void process(float delta) {

    }

}
