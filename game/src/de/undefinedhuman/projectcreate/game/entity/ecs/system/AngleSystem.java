package de.undefinedhuman.projectcreate.game.entity.ecs.system;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.angle.AngleComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteData;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class AngleSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private ComponentMapper<AngleComponent> am = ComponentMapper.getFor(AngleComponent.class);
    private ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);

    public AngleSystem() {
        super(0);
    }

    @Override
    public void addedToEngine (Engine engine) {
        entities = engine.getEntitiesFor(Family.all(AngleComponent.class, SpriteComponent.class).get());
    }

    @Override
    public void update(float delta) {
        TransformComponent transformComponent;
        AngleComponent angleComponent;
        SpriteComponent spriteComponent;

        for(Entity entity : entities) {
            transformComponent = Mappers.TRANSFORM.get(entity);
            angleComponent = Mappers.ANGLE.get(entity);
            spriteComponent = Mappers.SPRITE.get(entity);

            if (entity == GameManager.getInstance().player) {
                angleComponent.mousePos = Tools.getMouseCoordsInWorldSpace(CameraManager.gameCamera);
                boolean turned = angleComponent.mousePos.x < transformComponent.getCenterPosition().x;
                angleComponent.angle = ((turned ? -1 : 1) * angleComponent.angle) % 360;
                angleComponent.isTurned = !turned;
            }

            for (SpriteData data : spriteComponent.getSpriteData())
                data.setTurned(angleComponent.isTurned);
        }

    }

}
