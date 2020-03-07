package de.undefinedhuman.sandboxgame.entity.ecs.system;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.entity.ecs.System;
import de.undefinedhuman.sandboxgame.entity.ecs.components.animation.AnimationComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.animation.AnimationParam;
import de.undefinedhuman.sandboxgame.entity.ecs.components.sprite.SpriteComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.sprite.SpriteData;

public class AnimationSystem extends System {

    public static AnimationSystem instance;

    public AnimationSystem() {}

    @Override
    public void init(Entity entity) {

        SpriteComponent spriteComponent;
        AnimationComponent animationComponent;

        if ((spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE)) != null && (animationComponent = (AnimationComponent) entity.getComponent(ComponentType.ANIMATION)) != null) {
            for (AnimationParam animationParam : animationComponent.getAnimationParams())
                for (String s : animationParam.spriteDataName) spriteComponent.getSpriteData(s).createRegions();
        }

    }

    @Override
    public void update(float delta, Entity entity) {

        SpriteComponent spriteComponent;
        AnimationComponent animationComponent;

        if ((spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE)) != null && (animationComponent = (AnimationComponent) entity.getComponent(ComponentType.ANIMATION)) != null) {

            animationComponent.addAnimationTime(delta);
            AnimationParam currentParam = animationComponent.getCurrentAnimationParam();
            for (String name : currentParam.spriteDataName) {

                SpriteData data = spriteComponent.getSpriteData(name);
                if (data.isAnimated()) data.setSpriteByRegion(animationComponent.getCurrentAnimationFrameID());
                else data.setSpriteByRegion(0);

            }


        }

    }

    @Override
    public void render(SpriteBatch batch) {}

    @Override
    public void render(SpriteBatch batch, Entity entity) {

    }

}
