package de.undefinedhuman.sandboxgame.background;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.entity.ecs.components.movement.MovementComponent;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;

public class BackgroundManager extends Manager {

    public static BackgroundManager instance;

    private BackgroundLayer background;
    private BackgroundLayer mountain;

    public BackgroundManager() {
        if (instance == null) instance = this;
        background = new BackgroundLayer(new Vector2(320, 180));
        mountain = new BackgroundLayer(new Vector2(688, 162), "background/foreground/mountain.png");
    }

    @Override
    public void init() {
        super.init();
        Time.load();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        background.resize(width, height);
        mountain.resize(width, height);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        background.update(delta, Variables.HOUR_LENGTH);
        MovementComponent movement = (MovementComponent) GameManager.instance.player.getComponent(ComponentType.MOVEMENT);
        if(movement != null)
            mountain.update(delta, (movement.moveLeft ? 1f : movement.moveRight ? -1f : 0) * movement.getSpeed() * 1.1f * Main.guiScale);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        //background.render(batch, camera);
        mountain.render(batch, camera);
    }

    @Override
    public void delete() {
        super.delete();
        Time.delete();
        mountain.delete();
    }

}
