package de.undefinedhuman.sandboxgame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTexture;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;

import java.util.ArrayList;

public class Gui extends GuiComponent {

    protected GuiTexture texture;

    private ArrayList<GuiComponent> children = new ArrayList<>();

    public Gui(GuiTexture texture) {
        super();
        this.texture = texture;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.texture.resize(scale);
        for(GuiComponent component : children) component.resize(width, height);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for(GuiComponent component : children) component.update(delta);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        texture.render(batch, bounds.getValue(Axis.X), bounds.getValue(Axis.Y), bounds.getValue(Axis.WIDTH), bounds.getValue(Axis.HEIGHT), color);
        for(GuiComponent component : children) component.render(batch, camera);
    }

    @Override
    public void delete() {
        super.delete();
        texture.delete();
        for(GuiComponent component : children) component.delete();
    }

    public Gui addChild(GuiComponent... components) {
        for(GuiComponent component : components) {
            component.parent = this;
            component.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.children.add(component);
        }
        return this;
    }

}
