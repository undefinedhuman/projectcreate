package de.undefinedhuman.projectcreate.game.entity.ecs.system;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.collision.CollisionComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteData;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.game.entity.Entity;
import de.undefinedhuman.projectcreate.game.entity.ecs.System;

public class RenderSystem extends System {

    @Override
    public void init(Entity entity) {
        SpriteComponent spriteComponent;
        if((spriteComponent = (SpriteComponent) entity.getComponent(SpriteComponent.class)) == null) return;
        for(SpriteData data : spriteComponent.getSpriteData()) data.init(entity, entity.getSize());
    }

    @Override
    public void update(float delta, Entity entity) {}

    public void render(SpriteBatch batch, Entity entity, int renderOffset) {
        SpriteComponent spriteComponent;
        if((spriteComponent = (SpriteComponent) entity.getComponent(SpriteComponent.class)) == null) return;
        spriteComponent.render(batch, renderOffset);

        CollisionComponent collisionComponent = (CollisionComponent) entity.getComponent(CollisionComponent.class);
        if (!Variables.DEBUG || collisionComponent == null) return;
        collisionComponent.update(new Vector2(entity.getPosition()).add(renderOffset, 0));
        collisionComponent.getHitbox().render(batch);
    }

}
