package de.undefinedhuman.projectcreate.game.entity.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.type.TypeComponent;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;

import java.util.Comparator;

public class RenderSystem extends SortedIteratingSystem {

    private SpriteBatch batch;

    public RenderSystem(Engine engine) {
        super(Family.all(TransformComponent.class, SpriteComponent.class).get(), new TypeComparator(), 6);
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
    public void startProcessing() {
        batch.setProjectionMatrix(CameraManager.gameCamera.combined);
        batch.begin();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // TEMP IMPLEMENT CHUNK RENDERING
        int renderOffset = 0;
        Mappers.SPRITE.get(entity).render(batch, Mappers.TRANSFORM.get(entity), renderOffset);
    }

    @Override
    public void endProcessing() {
        batch.end();
    }

    private static class TypeComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity e1, Entity e2) {
            TypeComponent entity1TypeComponent = Mappers.TYPE.get(e1);
            TypeComponent entity2TypeComponent = Mappers.TYPE.get(e2);
            if(entity1TypeComponent == null)
                return -1;
            if(entity2TypeComponent == null)
                return 1;
            return Integer.compare(entity1TypeComponent.getType().ordinal(), entity2TypeComponent.getType().ordinal());
        }
    }

}
