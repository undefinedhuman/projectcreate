package de.undefinedhuman.sandboxgame.gui.transforms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.gui.GuiManager;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class GuiTransform {

    public GuiTransform parent;

    protected HashMap<Axis, Integer> currentValues = new HashMap<>();
    protected boolean visible = true;

    private HashMap<Axis, Constraint> constraints = new HashMap<>();

    public GuiTransform() {
        this.parent = GuiManager.instance.screen;
    }

    public void init() {}

    public GuiTransform initScreen() {
        this.parent = this;
        return this;
    }

    public void resize(int width, int height) {
        calculateCurrentValues(Axis.WIDTH, Axis.HEIGHT, Axis.X, Axis.Y);
    }

    public void update(float delta) {}

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if (!visible || !parent.isVisible()) return;
    }

    public void delete() {
        constraints.clear();
    }

    public GuiTransform set(Constraint x, Constraint y, Constraint width, Constraint height) {
        setPosition(x, y);
        setSize(width, height);
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

    public GuiTransform setCentered() {
        setOffset(new RelativeOffset(-0.5f), new RelativeOffset(-0.5f));
        return this;
    }

    public GuiTransform setCenteredX() {
        setOffsetX(new RelativeOffset(-0.5f));
        return this;
    }

    public GuiTransform setCenteredY() {
        setOffsetY(new RelativeConstraint(-0.5f));
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

    public GuiTransform setPosition(int x, int y) {
        this.currentValues.put(Axis.X, x);
        this.currentValues.put(Axis.Y, y);
        return this;
    }

    public GuiTransform setSize(int width, int height) {
        this.currentValues.put(Axis.WIDTH, width);
        this.currentValues.put(Axis.HEIGHT, height);
        return this;
    }

    public boolean isClicked(OrthographicCamera camera) {
        Vector2 coords = Tools.getMouseCoordsInWorldSpace(camera);
        return (coords.x >= getCurrentValue(Axis.X) && coords.x <= getCurrentValue(Axis.X) + getCurrentValue(Axis.WIDTH)) && (coords.y >= getCurrentValue(Axis.Y) && coords.y <= getCurrentValue(Axis.Y) + getCurrentValue(Axis.HEIGHT));
    }

    public GuiTransform setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean isVisible() { return visible; }

    public Vector2 getPosition() {
        return new Vector2(currentValues.get(Axis.X), currentValues.get(Axis.Y));
    }

    public Vector2 getSize() {
        return new Vector2(currentValues.get(Axis.WIDTH), currentValues.get(Axis.HEIGHT));
    }

    public int getCurrentValue(Axis axis) {
        Integer value = currentValues.get(axis);
        return value != null ? value : 0;
    }

    private void calculateCurrentValues(Axis... axes) {
        for(Axis axis : axes)
            this.currentValues.put(axis, calculateConstraintValue(axis));
    }

    private int calculateConstraintValue(Axis axis) {
        if (!constraints.containsKey(axis)) return 0;
        return constraints.get(axis).getValue(Main.guiScale);
    }

    private void addConstraint(Axis axis, Constraint constraint) {
        this.constraints.put(axis, constraint.setAxis(axis).setGui(this));
    }

}
