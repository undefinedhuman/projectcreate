package de.undefinedhuman.projectcreate.engine.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.gui.text.Text;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTexture;
import de.undefinedhuman.projectcreate.engine.gui.transforms.Axis;
import de.undefinedhuman.projectcreate.engine.gui.transforms.GuiTransform;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.projectcreate.engine.resources.font.Font;

import java.util.ArrayList;

public class Gui extends GuiComponent {

    protected GuiTexture texture;
    private ArrayList<GuiTransform> children = new ArrayList<>();

    public Gui() {
        this(new GuiTexture());
    }

    public Gui(String texture) {
        this(new GuiTexture(texture));
    }

    public Gui(GuiTemplate template) {
        this(new GuiTexture(template));
    }

    public Gui(GuiTexture texture) {
        super();
        this.texture = texture;
    }

    @Override
    public void init() {
        super.init();
        this.texture.init();
        for(GuiTransform transform : children)
            transform.init();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.texture.resize(getCurrentValue(Axis.WIDTH), getCurrentValue(Axis.HEIGHT), GuiManager.GUI_SCALE);
        for (GuiTransform transform : children)
            transform.resize(width, height);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for (GuiTransform transform : children) transform.update(delta);
    }

    private int count = 0;

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        if (!visible)
            return;
        texture.render(batch, getCurrentValue(Axis.X), getCurrentValue(Axis.Y), alpha);
        for (GuiTransform transform : children)
            transform.render(batch, camera);
    }

    @Override
    public void delete() {
        super.delete();
        texture.delete();
        deleteChildren();
    }

    @Override
    public GuiComponent setColor(Color color) {
        texture.setColor(color);
        return super.setColor(color);
    }

    public Gui setTitle(String titleString, Font font, Color color) {
        Text text = new Text(titleString);
        text.setFont(font).setFontSize(16).setColor(color).setPosition(new CenterConstraint(), new RelativeConstraint(1f)).setOffset(new CenterOffset(), new RelativeOffset(0.55f));
        addChild(text);
        return this;
    }

    public Gui addChild(GuiTransform... components) {
        for (GuiTransform component : components) {
            component.parent = this;
            component.init();
            component.resize();
            this.children.add(component);
        }
        return this;
    }

    public void removeChildren() {
        children.clear();
    }

    public void deleteChildren() {
        for(GuiTransform child : children)
            child.delete();
        children.clear();
    }

    public void setTexture(String texture) {
        this.texture.setTexture(texture);
    }

    public GuiTexture getTexture() {
        return texture;
    }

    public Vector2 getOffset() { return texture.getOffset(); }

    @Override
    public int getScaledCornerSize() {
        return texture.getScaledCornerSize();
    }

    public int getBaseCornerSize() {
        return texture.getBaseCornerSize();
    }

    public ArrayList<GuiTransform> getChildren() {
        return children;
    }

}
