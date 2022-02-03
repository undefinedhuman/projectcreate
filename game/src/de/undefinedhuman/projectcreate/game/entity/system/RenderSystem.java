package de.undefinedhuman.projectcreate.game.entity.system;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.EntityFlag;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.base.type.TypeComponent;
import de.undefinedhuman.projectcreate.core.ecs.stats.name.NameComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.All;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.Optional;
import de.undefinedhuman.projectcreate.engine.ecs.systems.SortedIteratingSystem;
import de.undefinedhuman.projectcreate.engine.gui.GuiManager;
import de.undefinedhuman.projectcreate.engine.gui.transforms.Axis;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;

import java.util.Arrays;
import java.util.Comparator;

@All({TransformComponent.class, SpriteComponent.class})
@Optional(TypeComponent.class)
public class RenderSystem extends SortedIteratingSystem {

    private SpriteBatch batch;

    private static final int NAME_TEXT_Y_OFFSET = 35;

    public RenderSystem() {
        super(new TypeComparator(), 6);
        batch = new SpriteBatch();
        EntityManager.getInstance().subscribe(EntityManager.EntityEvent.class, EntityManager.EntityEvent.Type.ADD, entities -> Arrays.stream(entities).filter(Mappers.NAME::has).map(Mappers.NAME::get).forEach(nameComponent -> GuiManager.getInstance().addGui(nameComponent.getText())));
        EntityManager.getInstance().subscribe(EntityManager.EntityEvent.class, EntityManager.EntityEvent.Type.REMOVE, entities -> {
            Arrays.stream(entities).filter(Mappers.SPRITE::has).map(Mappers.SPRITE::get).forEach(SpriteComponent::delete);
            Arrays.stream(entities).filter(Mappers.NAME::has).map(Mappers.NAME::get).forEach(nameComponent -> GuiManager.getInstance().removeGui(nameComponent.getText()));
        });
    }

    @Override
    public void start() {
        batch.setProjectionMatrix(CameraManager.gameCamera.combined);
        batch.begin();
    }

    @Override
    public void processEntity(float delta, Entity entity) {
        // TEMP IMPLEMENT CHUNK RENDERING
        TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
        int renderOffset = 0;
        Mappers.SPRITE.get(entity).render(batch, Mappers.TRANSFORM.get(entity), renderOffset);
        NameComponent nameComponent = Mappers.NAME.get(entity);
        if(nameComponent == null)
            return;
        Vector2 textPosition = Utils.calculateScreenFromWorldSpace(CameraManager.gameCamera, transformComponent.getCenterPosition().add(0, NAME_TEXT_Y_OFFSET));
        nameComponent.getText().setPosition((textPosition.x - nameComponent.getText().getCurrentValue(Axis.WIDTH)/2f), textPosition.y);
        nameComponent.getText().resize();
    }

    @Override
    public void end() {
        batch.end();
    }

    private static class TypeComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity e1, Entity e2) {
            TypeComponent entity1TypeComponent = Mappers.TYPE.get(e1);
            TypeComponent entity2TypeComponent = Mappers.TYPE.get(e2);
            if(entity1TypeComponent == null || EntityFlag.hasFlags(e2, EntityFlag.MAIN_PLAYER)) return -1;
            if(entity2TypeComponent == null || EntityFlag.hasFlags(e1, EntityFlag.MAIN_PLAYER)) return 1;
            return Integer.compare(entity1TypeComponent.getType().ordinal(), entity2TypeComponent.getType().ordinal());
        }
    }

}
