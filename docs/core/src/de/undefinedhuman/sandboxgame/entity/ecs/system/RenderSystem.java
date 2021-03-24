package de.undefinedhuman.sandboxgame.entity.ecs.system;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.components.collision.CollisionComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteData;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.System;

public class RenderSystem extends System {

    public static RenderSystem instance;

    public RenderSystem() {
        if (instance == null) instance = this;
    }

    @Override
    public void init(Entity entity) {
        SpriteComponent spriteComponent;
        if((spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE)) == null) return;
        for(SpriteData data : spriteComponent.getSpriteData()) data.init(entity, entity.getSize());
    }

    @Override
    public void update(float delta, Entity entity) {}

    public void render(SpriteBatch batch, Entity entity, int renderOffset) {
        SpriteComponent spriteComponent;
        if((spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE)) == null) return;
        spriteComponent.render(batch, renderOffset);

        CollisionComponent collisionComponent = (CollisionComponent) entity.getComponent(ComponentType.COLLISION);
        if (!Variables.DEBUG || collisionComponent == null) return;
        collisionComponent.update(new Vector2(entity.getPosition()).add(renderOffset, 0));
        collisionComponent.getHitbox().render(batch);
    }

}
