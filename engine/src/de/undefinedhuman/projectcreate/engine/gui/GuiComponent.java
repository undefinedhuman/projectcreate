package de.undefinedhuman.projectcreate.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.engine.gui.event.ClickListener;
import de.undefinedhuman.projectcreate.engine.gui.event.Listener;
import de.undefinedhuman.projectcreate.engine.gui.transforms.GuiTransform;

import java.util.ArrayList;

public class GuiComponent extends GuiTransform {

    protected float alpha = 1;
    protected Color color = new Color(Color.WHITE);

    protected ArrayList<Listener> listeners = new ArrayList<>();

    private boolean isClicked = false;

    public GuiComponent() {
        super();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (!Gdx.input.isButtonPressed(0) && isClicked)
            isClicked = false;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        if(!visible)
            return;

        if(!isClicked(camera) || isClicked || !Gdx.input.isButtonPressed(0))
            return;
        isClicked = true;
        listeners.stream().filter(ClickListener.class::isInstance).map(ClickListener.class::cast).forEach(ClickListener::onClick);
    }

    @Override
    public void delete() {
        listeners.clear();
    }

    public GuiComponent setAlpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    public GuiComponent setColor(Color color) {
        this.color = color;
        resize();
        return this;
    }

    public GuiComponent addListener(Listener listener) {
        this.listeners.add(listener);
        return this;
    }

}
