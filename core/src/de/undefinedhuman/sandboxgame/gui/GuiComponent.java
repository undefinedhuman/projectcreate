package de.undefinedhuman.sandboxgame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.Bounds;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraints;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class GuiComponent {

    public GuiComponent parent;

    protected Bounds bounds = new Bounds();
    protected Constraints constraints;
    protected boolean visible = true, init = false;
    protected float baseScale = 1, scale, alpha = 1;
    protected Color color = new Color(Color.WHITE);

    public GuiComponent() {
        this.parent = GuiManager.instance.screen;
        this.constraints = new Constraints();
    }

    public void resize(int width, int height) {
        if(!init) return;
        scale = Math.max((int) Math.ceil(Main.guiScale * baseScale), 1);
        this.bounds.resize(constraints, scale);
    }

    public void init() {}

    public void update(float delta) { }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if(!visible || !parent.visible || !init) return;
    }

    public void delete() {
        if(constraints != null) constraints.delete();
    }

    public GuiComponent setConstraints(Constraints constraints) {
        this.constraints = constraints.setGui(this);
        init = true;
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

    public GuiComponent setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public GuiComponent setScale(float scale) {
        this.baseScale = scale;
        if(init) resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

    public GuiComponent setAlpha(float alpha) {
        this.alpha = alpha;
        this.color.a = alpha;
        return this;
    }

    public GuiComponent setColor(Color color) {
        float alpha = this.color.a;
        this.color = new Color(color.r, color.g, color.b, alpha);
        return this;
    }

    public GuiComponent initScreen(float width, float height) {
        this.parent = this;
        this.init = true;
        this.bounds.initScreen(width, height);
        return this;
    }

    public boolean isClicked(OrthographicCamera camera) {
        Vector2 coords = Tools.getAbsoluteMouse(camera);
        float x = getBound(Axis.X), y = getBound(Axis.Y);
        return (coords.x >= x && coords.x <= x + getBound(Axis.WIDTH)) && (coords.y >= y && coords.y <= y + getBound(Axis.HEIGHT));
    }

    public Vector2 getPosition() { return new Vector2(getBound(Axis.X), getBound(Axis.Y)); }
    public Vector2 getScale() { return new Vector2(getBound(Axis.WIDTH), getBound(Axis.HEIGHT)); }
    public boolean isVisible() { return visible; }
    public int getBound(Axis axis) { return bounds.getValue(axis); }
    public Constraints getConstraints() { return constraints; }
    public Bounds getParentBounds() { return parent.getBounds(); }
    public Bounds getBounds() { return bounds; }
    public GuiComponent getParent() { return parent; }

}
