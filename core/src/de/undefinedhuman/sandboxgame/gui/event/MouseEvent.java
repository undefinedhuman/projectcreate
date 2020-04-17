package de.undefinedhuman.sandboxgame.gui.event;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class MouseEvent extends Event {

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        Vector2 pos = Tools.getMouseCoordsInWorldSpace(camera);
        this.guiComponent.setPosition((int) pos.x, (int) pos.y);
    }

}
