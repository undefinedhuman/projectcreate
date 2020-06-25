package de.undefinedhuman.sandboxgame.gui.transforms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.utils.math.Vector4;
import de.undefinedhuman.sandboxgame.gui.GuiManager;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.ConstantConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.PixelOffset;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class GuiTransform {

    public GuiTransform parent;

    protected Vector2 position = new Vector2(), size = new Vector2();
    protected boolean visible = true;

    private HashMap<Axis, Constraint> constraints = new HashMap<>();

    public GuiTransform() {
        this.parent = GuiManager.instance.screen;
    }

    public void init() {}

    public void resize(int width, int height) {
        this.size.set(calculateConstraintValue(Axis.WIDTH), calculateConstraintValue(Axis.HEIGHT));
        this.position.set(calculateConstraintValue(Axis.X) + calculateConstraintValue(Axis.OFFSET_X), calculateConstraintValue(Axis.Y) + calculateConstraintValue(Axis.OFFSET_Y));
    }

    private int calculateConstraintValue(Axis axis) {
        if (!constraints.containsKey(axis)) return 0;
        return constraints.get(axis).getValue(Main.guiScale);
    }

    public void update(float delta) {}

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if (!visible || !parent.isVisible()) return;
    }

    public void delete() {
        constraints.clear();
    }

    public GuiTransform set(Constraint x, Constraint y, Constraint width, Constraint height) {
        addConstraint(Axis.X, x);
        addConstraint(Axis.Y, y);
        addConstraint(Axis.WIDTH, width);
        addConstraint(Axis.HEIGHT, height);
        return this;
    }

    public GuiTransform setPosition(Constraint x, Constraint y) {
        addConstraint(Axis.X, x);
        addConstraint(Axis.Y, y);
        return this;
    }

    public GuiTransform setSize(Constraint width, Constraint height) {
        addConstraint(Axis.WIDTH, width);
        addConstraint(Axis.HEIGHT, height);
        return this;
    }

    public GuiTransform setOffset(Constraint x, Constraint y) {
        setOffsetX(x);
        setOffsetY(y);
        return this;
    }

    public GuiTransform setOffsetX(Constraint x) {
        addConstraint(Axis.OFFSET_X, x);
        return this;
    }

    public GuiTransform setOffsetY(Constraint y) {
        addConstraint(Axis.OFFSET_Y, y);
        return this;
    }

    public GuiTransform setValue(Axis axis, float value) {
        constraints.get(axis).setValue(value);
        return this;
    }

    public float getBaseValue(Axis axis) {
        if (!constraints.containsKey(axis)) return 0;
        return constraints.get(axis).getValue();
    }

    public GuiTransform initScreen(int width, int height) {
        this.parent = this;
        setPosition(new PixelConstraint(0), new PixelConstraint(0));
        setSize(new PixelConstraint(width), new PixelConstraint(height));
        return this;
    }

    public boolean isClicked(OrthographicCamera camera) {
        Vector2 coords = Tools.getMouseCoordsInWorldSpace(camera);
        return (coords.x >= position.x && coords.x <= position.x + scale.x) && (coords.y >= position.y && coords.y <= position.y + scale.y);
    }

    public GuiTransform setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean isVisible() { return visible; }

    public Vector2 getPosition() {
        return new Vector2(position);
    }

    public Vector2 getScale() {
        return new Vector2(size);
    }

    private void addConstraint(Axis axis, Constraint constraint) {
        this.constraints.put(axis, constraint.setAxis(axis).setGui(this));
    }

}
