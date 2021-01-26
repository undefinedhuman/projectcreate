package de.undefinedhuman.sandboxgame.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.gui.event.Event;
import de.undefinedhuman.sandboxgame.gui.transforms.GuiTransform;

import java.util.ArrayList;

public class GuiComponent extends GuiTransform {

    protected float alpha = 1;
    protected Color color = new Color(Color.WHITE);

    private ArrayList<Event> events = new ArrayList<>();

    public GuiComponent() {
        super();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for (Event event : events) event.update(delta);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        for (Event event : events) event.render(batch, camera);
    }

    @Override
    public void delete() {
        events.clear();
    }

    public GuiComponent setAlpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    public GuiComponent setColor(Color color) {
        this.color = color;
        return this;
    }

    public GuiComponent addEvent(Event event) {
        this.events.add(event);
        event.setGuiComponent(this);
        return this;
    }

}
