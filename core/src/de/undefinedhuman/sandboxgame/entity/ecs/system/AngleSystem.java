package de.undefinedhuman.sandboxgame.entity.ecs.system;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.components.mouse.AngleComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteData;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.System;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class AngleSystem extends System {

    public static AngleSystem instance;

    public AngleSystem() {
        if (instance == null) instance = this;
    }

    @Override
    public void init(Entity entity) {}

    @Override
    public void update(float delta, Entity entity) {

        AngleComponent angleComponent;
        SpriteComponent spriteComponent;

        if ((angleComponent = (AngleComponent) entity.getComponent(ComponentType.ANGLE)) != null) {

            if (entity.mainPlayer) {

                Vector2 mousePos = Tools.getWorldCoordsOfMouse(GameManager.gameCamera);

                float angle = angleComponent.angle;
                boolean turned = angleComponent.isTurned, mouse = mousePos.x < entity.getPosition().x + entity.getSize().x / 2;

                angleComponent.angle = mouse ? (turned ? -(angle) : angle) : (!turned ? -(angle) : angle);
                angleComponent.isTurned = !(mousePos.x < entity.getPosition().x + entity.getSize().x / 2);

                angleComponent.angle = Tools.angleCrop(angleComponent.angle);

            }

            if ((spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE)) != null)
                for (SpriteData data : spriteComponent.getSpriteDataValue())
                    data.setScale(new Vector2(angleComponent.isTurned ? Math.abs(data.getScale().x) : -Math.abs(data.getScale().x), data.getScale().y));

        }

    }

    @Override
    public void render(SpriteBatch batch) {
    }

    @Override
    public void render(SpriteBatch batch, Entity entity) {
    }

}
