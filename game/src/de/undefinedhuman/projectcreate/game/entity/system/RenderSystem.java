package de.undefinedhuman.projectcreate.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.name.NameComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.type.TypeComponent;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.engine.gui.GuiManager;
import de.undefinedhuman.projectcreate.engine.gui.transforms.Axis;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;

import java.util.Comparator;

public class RenderSystem extends SortedIteratingSystem {

    private SpriteBatch batch;

    public RenderSystem() {
        super(Family.all(TransformComponent.class, SpriteComponent.class).get(), new TypeComparator(), 6);
        batch = new SpriteBatch();
        EntityManager.getInstance().addEntityListener(new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
                SpriteComponent spriteComponent = Mappers.SPRITE.get(entity);
                if(spriteComponent != null)
                    spriteComponent.init();
                NameComponent nameComponent = Mappers.NAME.get(entity);
                if(nameComponent != null)
                    GuiManager.getInstance().addGui(nameComponent.getText());
            }

            @Override
            public void entityRemoved(Entity entity) {
                SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
                if(spriteComponent != null) spriteComponent.delete();
                NameComponent nameComponent = Mappers.NAME.get(entity);
                if(nameComponent != null)
                    GuiManager.getInstance().removeGui(nameComponent.getText());
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
        TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
        int renderOffset = 0;
        Mappers.SPRITE.get(entity).render(batch, Mappers.TRANSFORM.get(entity), renderOffset);
        NameComponent nameComponent = Mappers.NAME.get(entity);
        if(nameComponent == null)
            return;
        Vector2 textPosition = Utils.calculateScreenFromWorldSpace(CameraManager.gameCamera, transformComponent.getCenterPosition().add(0, 35));
        nameComponent.getText().setCurrentPosition((int) textPosition.x - nameComponent.getText().getCurrentValue(Axis.WIDTH)/2, (int) textPosition.y);
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
