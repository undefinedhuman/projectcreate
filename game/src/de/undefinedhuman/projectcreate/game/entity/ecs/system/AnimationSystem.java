package de.undefinedhuman.projectcreate.game.entity.ecs.system;

import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.entity.components.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.engine.entity.components.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.engine.entity.components.sprite.SpriteData;
import de.undefinedhuman.projectcreate.game.entity.Entity;
import de.undefinedhuman.projectcreate.game.entity.ecs.System;

public class AnimationSystem extends System {

    @Override
    public void update(float delta, Entity entity) {
        SpriteComponent spriteComponent;
        AnimationComponent animationComponent;
        if ((spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE)) == null
                || (animationComponent = (AnimationComponent) entity.getComponent(ComponentType.ANIMATION)) == null) return;

        animationComponent.addAnimationTime(delta);
        for (SpriteData data : spriteComponent.getSpriteData()) data.setFrameIndex(animationComponent.getAnimationFrameIndex());
    }

}
