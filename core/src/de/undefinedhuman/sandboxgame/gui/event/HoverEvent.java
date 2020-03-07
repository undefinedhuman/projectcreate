package de.undefinedhuman.sandboxgame.gui.event;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class HoverEvent extends Event {

    private float time = 0, delayTime;

    private boolean over = false;

    public HoverEvent(float delayTime) {
        this.delayTime = delayTime;
    }

    @Override
    public void update(float delta) {
        if (over) time += delta;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {

        if (guiComponent.isClicked(camera) && guiComponent.isVisible()) {
            over = true;
            if (time >= delayTime) onMouseEnter(batch);
        } else if (over) {
            time = 0;
            over = false;
            onMouseLeave();
        }

    }

    public abstract void onMouseEnter(SpriteBatch batch);

    public abstract void onMouseLeave();

}
