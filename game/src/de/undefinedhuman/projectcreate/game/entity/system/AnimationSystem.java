package de.undefinedhuman.projectcreate.game.entity.system;

import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.visual.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.All;
import de.undefinedhuman.projectcreate.engine.ecs.systems.IteratingSystem;

@All({SpriteComponent.class, AnimationComponent.class})
public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() {
        super(1);
    }

    @Override
    public void processEntity(float delta, Entity entity) {
        Mappers.SPRITE.get(entity).setFrameIndex(Mappers.ANIMATION.get(entity).addAnimationTime(delta).getAnimationFrameIndex());
    }

}
