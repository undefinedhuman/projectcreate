package de.undefinedhuman.projectcreate.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.visual.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteComponent;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() {
        super(Family.all(SpriteComponent.class, AnimationComponent.class).get(), 1);
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        Mappers.SPRITE.get(entity).setFrameIndex(Mappers.ANIMATION.get(entity).addAnimationTime(delta).getAnimationFrameIndex());
    }

}
