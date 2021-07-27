package de.undefinedhuman.projectcreate.game.entity.ecs.system;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;

public class RenderSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private SpriteBatch batch;

    public RenderSystem(Engine engine) {
        super(6);
        batch = new SpriteBatch();
        engine.addEntityListener(new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
                SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
                if(spriteComponent != null)
                    spriteComponent.init();
            }

            @Override
            public void entityRemoved(Entity entity) {
                SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
                if(spriteComponent != null) spriteComponent.delete();
            }
        });
    }

    @Override
    public void addedToEngine (Engine engine) {
        entities = engine.getEntitiesFor(Family.all(TransformComponent.class, SpriteComponent.class).get());
    }

    @Override
    public void update (float deltaTime) {
        batch.setProjectionMatrix(CameraManager.gameCamera.combined);
        batch.begin();
        entities.forEach(entity -> {
            // TEMP IMPLEMENT CHUNK RENDERING
            int renderOffset = 0;
            Mappers.SPRITE.get(entity).render(batch, Mappers.TRANSFORM.get(entity), renderOffset);
        });
        batch.end();
    }

}
