package de.undefinedhuman.sandboxgame.entity.ecs.system;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.components.animation.AnimationComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteData;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.System;

import java.util.HashMap;

public class AnimationSystem extends System {

    public static AnimationSystem instance;

    public AnimationSystem() {
        if (instance == null) instance = this;
    }

    @Override
    public void init(Entity entity) {

    }

    @Override
    public void update(float delta, Entity entity) {
        SpriteComponent spriteComponent;
        AnimationComponent animationComponent;

        if ((spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE)) == null || (animationComponent = (AnimationComponent) entity.getComponent(ComponentType.ANIMATION)) == null) return;

        animationComponent.addAnimationTime(delta);
        HashMap<String, SpriteData> spriteData = spriteComponent.getSpriteData();
        for (String key : spriteData.keySet()) {
            SpriteData data = spriteComponent.getSpriteDataValue(key);
            data.setFrameIndex(animationComponent.getAnimationFrameIndex());
        }
    }

    @Override
    public void render(SpriteBatch batch) {}

    @Override
    public void render(SpriteBatch batch, Entity entity) {}

}
