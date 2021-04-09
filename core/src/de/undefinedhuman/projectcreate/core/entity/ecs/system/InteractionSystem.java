package de.undefinedhuman.projectcreate.core.entity.ecs.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.entity.components.interaction.InteractionComponent;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.core.entity.Entity;
import de.undefinedhuman.projectcreate.core.entity.ecs.System;
import de.undefinedhuman.projectcreate.core.gui.GuiManager;
import de.undefinedhuman.projectcreate.core.gui.text.Text;
import de.undefinedhuman.projectcreate.core.screen.gamescreen.GameManager;

public class InteractionSystem extends System {

    private Vector2 distanceVector = new Vector2();
    private Text helpText;

    @Override
    public void init(Entity entity) {
        helpText = new Text("");
        GuiManager.instance.addGui(helpText.setPosition(new RelativeConstraint(0.5f), new PixelConstraint(50)).setVisible(true));
    }

    @Override
    public void update(float delta, Entity entity) {

        InteractionComponent interactionComponent;

        helpText.setVisible(false);

        if ((interactionComponent = (InteractionComponent) entity.getComponent(ComponentType.INTERACTION)) == null) return;

        if (distanceVector.set(entity.getCenterPosition()).dst(GameManager.instance.player.getCenterPosition()) < interactionComponent.getRange()) {

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
