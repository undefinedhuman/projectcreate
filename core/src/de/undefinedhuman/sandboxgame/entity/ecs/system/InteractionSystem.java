package de.undefinedhuman.sandboxgame.entity.ecs.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.entity.ecs.System;
import de.undefinedhuman.sandboxgame.entity.ecs.components.interaction.InteractionComponent;
import de.undefinedhuman.sandboxgame.gui.GuiManager;
import de.undefinedhuman.sandboxgame.gui.text.Text;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;

public class InteractionSystem extends System {

    public static InteractionSystem instance;
    private Vector2 distanceVector = new Vector2();
    private Text helpText;

    public InteractionSystem() {
        if (instance == null) instance = this;
        helpText = new Text("");
        GuiManager.instance.addGui(helpText.setGuiScale(1f).setPosition("r0.5", "p50").setCentered().setVisible(true));
    }

    @Override
    public void init(Entity entity) {}

    @Override
    public void update(float delta, Entity entity) {

        InteractionComponent interactionComponent;

        helpText.setVisible(false);

        if ((interactionComponent = (InteractionComponent) entity.getComponent(ComponentType.INTERACTION)) != null) {

            if (distanceVector.set(entity.transform.getCenterPosition()).dst(GameManager.instance.player.transform.getCenterPosition()) < interactionComponent.getRange()) {

                helpText.setVisible(true);
                helpText.setText("Press " + Input.Keys.toString(interactionComponent.getInputKey()) + " for interaction!");

                if (!interactionComponent.pressed && Gdx.input.isKeyPressed(interactionComponent.getInputKey())) {
                    Log.info("INTERACTION!");
                    interactionComponent.pressed = true;
                }

            }

            if (interactionComponent.pressed && !Gdx.input.isKeyPressed(interactionComponent.getInputKey()))
                interactionComponent.pressed = false;

        }

    }

    @Override
    public void render(SpriteBatch batch) {

    }

}
