package de.undefinedhuman.sandboxgame.gui.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class ClickEvent extends Event {

    private boolean isClicked = false;

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {

        if (!Gdx.input.isButtonPressed(0) && isClicked) isClicked = false;

        if(!isClicked && Gdx.input.isButtonPressed(0) && guiComponent.isVisible() && guiComponent.isClicked(camera)) {
            isClicked = true;
            onClick();
        }

    }

    public abstract void onClick();

}
