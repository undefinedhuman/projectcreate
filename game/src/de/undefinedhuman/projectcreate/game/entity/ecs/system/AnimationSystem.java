package de.undefinedhuman.projectcreate.game.entity.ecs.system;

import de.undefinedhuman.projectcreate.core.ecs.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteData;
import de.undefinedhuman.projectcreate.engine.ecs.system.System;

public class AnimationSystem extends System {

    @Override
    public void update(float delta, Entity entity) {
        SpriteComponent spriteComponent;
        AnimationComponent animationComponent;
        if ((spriteComponent = (SpriteComponent) entity.getComponent(SpriteComponent.class)) == null
                || (animationComponent = (AnimationComponent) entity.getComponent(AnimationComponent.class)) == null) return;

        animationComponent.addAnimationTime(delta);
        for (SpriteData data : spriteComponent.getSpriteData()) data.setFrameIndex(animationComponent.getAnimationFrameIndex());
    }

}
