package de.undefinedhuman.projectcreate.game.entity.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.interaction.InteractionComponent;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;

public class InteractionSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private Vector2 distanceVector = new Vector2();

    public InteractionSystem() {
        super(3);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(TransformComponent.class, InteractionComponent.class).get());
    }

    /*@Override
    public void init(Entity entity) {
        helpText = new Text("");
        GuiManager.getInstance().addGui(helpText.setPosition(new RelativeConstraint(0.5f), new PixelConstraint(50)).setVisible(true));
    }*/

    @Override
    public void update(float delta) {

        TransformComponent transformComponent;
        InteractionComponent interactionComponent;

        for(Entity entity : entities) {
            transformComponent = Mappers.TRANSFORM.get(entity);
            interactionComponent = Mappers.INTERACTION.get(entity);

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

}
