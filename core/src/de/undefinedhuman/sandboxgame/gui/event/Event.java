package de.undefinedhuman.sandboxgame.gui.event;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.gui.GuiComponent;

public abstract class Event {

    protected GuiComponent guiComponent;

    public void setGuiComponent(GuiComponent guiComponent) {
        this.guiComponent = guiComponent;
    }

    public abstract void update(float delta);
    public abstract void render(SpriteBatch batch, OrthographicCamera camera);

}
