package de.undefinedhuman.projectcreate.engine.gui.transforms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.gui.GuiManager;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.Constraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.Offset;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

import java.util.Arrays;
import java.util.HashMap;

public class GuiTransform {

    public GuiTransform parent;

    protected int[] currentValues = new int[Axis.values().length];
    protected boolean visible = true;

    private HashMap<Axis, Constraint> constraints = new HashMap<>();

    public GuiTransform() {
        this.parent = GuiManager.getInstance().screen;
        set(new PixelConstraint(0), new PixelConstraint(0), new PixelConstraint(0), new PixelConstraint(0));
        Arrays.fill(currentValues, 0);
    }

    public void init() { }

    public GuiTransform initScreen() {
        this.parent = this;
        return this;
    }

    public void resize() {
        this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void resize(int width, int height) {
        calculateCurrentValues(Axis.WIDTH, Axis.HEIGHT);
        currentValues[Axis.X.ordinal()] = calculateConstraintValue(Axis.X) + calculateConstraintValue(Axis.OFFSET_X);
        currentValues[Axis.Y.ordinal()] = calculateConstraintValue(Axis.Y) + calculateConstraintValue(Axis.OFFSET_Y);
    }

    public void update(float delta) {}

    public void render(SpriteBatch batch, OrthographicCamera camera) {}

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

    public GuiTransform setOffset(Offset x, Offset y) {
        setOffsetX(x);
        setOffsetY(y);
        return this;
    }

    public GuiTransform setOffsetX(Offset x) {
        addConstraint(Axis.OFFSET_X, x);
        return this;
    }

    public GuiTransform setOffsetY(Offset y) {
        addConstraint(Axis.OFFSET_Y, y);
        return this;
    }

    public Constraint getConstraint(Axis axis) {
        return constraints.get(axis);
    }

    public GuiTransform setPosition(float x, float y) {
        constraints.get(Axis.X).setValue(x);
        constraints.get(Axis.Y).setValue(y);
        return this;
    }

    public GuiTransform setSize(float width, float height) {
        constraints.get(Axis.WIDTH).setValue(width);
        constraints.get(Axis.HEIGHT).setValue(height);
        return this;
    }

    public GuiTransform setValue(Axis axis, float value) {
        constraints.get(axis).setValue(value);
        return this;
    }

    public float getBaseValue(Axis axis) {
        if (!constraints.containsKey(axis)) return 0;
        return constraints.get(axis).getBaseValue();
    }

    public boolean isClicked(OrthographicCamera camera) {
        Vector2 coords = Utils.calculateWorldFromScreenSpace(camera, Gdx.input.getX(), Gdx.input.getY());
        return (coords.x >= getCurrentValue(Axis.X) && coords.x <= getCurrentValue(Axis.X) + getCurrentValue(Axis.WIDTH)) && (coords.y >= getCurrentValue(Axis.Y) && coords.y <= getCurrentValue(Axis.Y) + getCurrentValue(Axis.HEIGHT));
    }

    public GuiTransform setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean isVisible() { return visible; }

    public Vector2 getPosition() {
        return new Vector2(currentValues[Axis.X.ordinal()], currentValues[Axis.Y.ordinal()]);
    }

    public Vector2 getSize() {
        return new Vector2(currentValues[Axis.WIDTH.ordinal()], currentValues[Axis.HEIGHT.ordinal()]);
    }

    public int getCurrentValue(Axis axis) {
        return currentValues[axis.ordinal()];
    }

    private void calculateCurrentValues(Axis... axes) {
        Arrays.fill(currentValues, 0);
        for(Axis axis : axes)
            currentValues[axis.ordinal()] = calculateConstraintValue(axis);
    }

    public int calculateConstraintValue(Axis axis) {
        return calculateConstraintValue(axis, GuiManager.GUI_SCALE);
    }

    public int calculateConstraintValue(Axis axis, int scale) {
        if (!constraints.containsKey(axis)) return 0;
        return constraints.get(axis).getValue(scale);
    }

    private void addConstraint(Axis axis, Constraint constraint) {
        this.constraints.put(axis, constraint.setAxis(axis).setGui(this));
    }

    public int getScaledCornerSize() {
        return 0;
    }

}
