package de.undefinedhuman.projectcreate.game.entity.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.interaction.InteractionComponent;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.All;
import de.undefinedhuman.projectcreate.engine.ecs.systems.IteratingSystem;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;

@All({TransformComponent.class, InteractionComponent.class})
public class InteractionSystem extends IteratingSystem {

    private Vector2 distanceVector = new Vector2();

    public InteractionSystem() {
        super(3);
    }

    @Override
    public void processEntity(float delta, Entity entity) {
        TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
        InteractionComponent interactionComponent = Mappers.INTERACTION.get(entity);

        if (distanceVector.set(transformComponent.getCenterPosition()).dst(GameManager.getInstance().player.getComponent(TransformComponent.class).getCenterPosition()) < interactionComponent.getRange()) {

            //helpText.setVisible(true);
            //helpText.setText("Press " + Input.Keys.toString(interactionComponent.getInputKey()) + " for interaction!");

            if (!interactionComponent.pressed && Gdx.input.isKeyPressed(interactionComponent.getInputKey())) {
                Log.info("INTERACTION!");
                interactionComponent.pressed = true;
            }

        }

        if (interactionComponent.pressed && !Gdx.input.isKeyPressed(interactionComponent.getInputKey()))
            interactionComponent.pressed = false;
    }

}
