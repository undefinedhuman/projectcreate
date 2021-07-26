package de.undefinedhuman.projectcreate.game.entity.ecs.system;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;

public class RenderSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    public RenderSystem (OrthographicCamera camera) {
        super(6);
        batch = new SpriteBatch();
        this.camera = camera;
    }

    @Override
    public void addedToEngine (Engine engine) {
        entities = engine.getEntitiesFor(Family.all(TransformComponent.class, SpriteComponent.class).get());
    }

    @Override
    public void update (float deltaTime) {
        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for (Entity entity : entities) {
            // TEMP IMPLEMENT CHUNK RENDERING
            int renderOffset = 0;
            Mappers.SPRITE.get(entity).render(batch, Mappers.TRANSFORM.get(entity), renderOffset);
        }

        batch.end();
    }

}
