package de.undefinedhuman.sandboxgame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.resources.font.Font;
import de.undefinedhuman.sandboxgame.gui.text.Text;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTexture;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.GuiTransform;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.PixelOffset;

import java.util.ArrayList;

public class Gui extends GuiComponent {

    protected GuiTexture texture;

    private ArrayList<GuiTransform> children = new ArrayList<>();

    public Gui(String texture) {
        this(new GuiTexture(texture));
    }

    public Gui(GuiTexture texture) {
        super();
        this.texture = texture;
    }

    public Gui(GuiTemplate template) {
        this(new GuiTexture(template));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.texture.resize(getCurrentValue(Axis.X), getCurrentValue(Axis.Y), getCurrentValue(Axis.WIDTH), getCurrentValue(Axis.HEIGHT));
        for (GuiTransform component : children) component.resize(width, height);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for (GuiTransform component : children) component.update(delta);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        if (!visible) return;
        texture.render(batch, alpha);
        for (GuiTransform component : children) component.render(batch, camera);
    }

    @Override
    public void delete() {
        super.delete();
        texture.delete();
        for (GuiTransform component : children) component.delete();
    }

    @Override
    public GuiComponent setColor(Color color) {
        texture.setColor(color);
        return super.setColor(color);
    }

    public void setTitle(String titleString, Font font, Color color) {
        Text text = new Text(titleString);
        text.setFont(font).setColor(color).setPosition(new RelativeConstraint(0.5f), new RelativeConstraint(1f)).setOffset(new CenterOffset(), new PixelOffset(getTemplate() == null ? 0 : -(getTemplate().cornerSize/2f)));
        addChild(text);
    }

    public Gui addChild(GuiTransform... components) {
        for (GuiTransform component : components) {
            component.parent = this;
            component.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.children.add(component);
        }
        return this;
    }

    public void clearChildren() {
        for(GuiTransform transform : children) transform.delete();
        children.clear();
    }

    @Override
    public GuiTransform setPosition(int x, int y) {
        texture.resize(x, y, getCurrentValue(Axis.WIDTH), getCurrentValue(Axis.HEIGHT));
        return super.setPosition(x, y);
    }

    @Override
    public GuiTransform setSize(int width, int height) {
        texture.resize(getCurrentValue(Axis.X), getCurrentValue(Axis.Y), width, height);
        return super.setSize(width, height);
    }

    public void setTexture(String texture) {
        this.texture.setTexture(texture);
    }

    public GuiTemplate getTemplate() {
        return texture.getTemplate();
    }

    public Vector2 getOffset() { return texture.getOffset(); }

    public int getCornerSize() { return texture.getCornerSize(); }

    public int getBaseCornerSize() { return texture.getBaseCornerSize(); }

}
