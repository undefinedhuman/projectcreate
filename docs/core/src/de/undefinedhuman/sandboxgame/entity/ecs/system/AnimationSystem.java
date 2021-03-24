package de.undefinedhuman.sandboxgame.entity.ecs.system;

import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.components.animation.AnimationComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteData;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.System;

public class AnimationSystem extends System {

    public static AnimationSystem instance;

    public AnimationSystem() {
        if (instance == null) instance = this;
    }

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
