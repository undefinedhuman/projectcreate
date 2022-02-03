package de.undefinedhuman.projectcreate.core.ecs.item;

import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.RequiredComponents;

@RequiredComponents({TransformBlueprint.class, SpriteBlueprint.class})
public class DropItemBlueprint extends ComponentBlueprint<DropItemComponent> {
    @Override
    public DropItemComponent createInstance() {
        return new DropItemComponent();
    }
}
