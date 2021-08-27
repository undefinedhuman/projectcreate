package de.undefinedhuman.projectcreate.core.ecs.system;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import de.undefinedhuman.projectcreate.core.ecs.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteComponent;

public class AnimationSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);
    private ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);

    public AnimationSystem() {
        super(1);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(AnimationComponent.class, SpriteComponent.class).get());
    }

    @Override
    public void update(float delta) {
        SpriteComponent spriteComponent;
        AnimationComponent animationComponent;

        for(Entity entity : entities) {
            spriteComponent = sm.get(entity);
            animationComponent = am.get(entity);

            animationComponent.addAnimationTime(delta);
            spriteComponent.setFrameIndex(animationComponent.getAnimationFrameIndex());
        }
    }

}
