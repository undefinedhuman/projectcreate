package de.undefinedhuman.sandboxgame.gui.transforms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.Main;
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

    protected float guiScale = 1, baseGuiScale = 0.5f;
    protected Vector2 position = new Vector2(), scale = new Vector2(), centerOffset = new Vector2();
    protected boolean calcScale = true, visible = true;

    private HashMap<Axis, Constraint> constraints = new HashMap<>();

    public GuiTransform() {
        this.parent = GuiManager.instance.screen;
    }

    public void init() {}

    public void resize(int width, int height) {
        this.guiScale = Math.max((int) Math.ceil(Main.guiScale * baseGuiScale), 1);
        if(calcScale) this.scale.set(getConstraint(Axis.WIDTH), getConstraint(Axis.HEIGHT));
        this.position.set(getConstraint(Axis.X) + getConstraint(Axis.OFFSET_X) + scale.x * centerOffset.x, getConstraint(Axis.Y) + getConstraint(Axis.OFFSET_Y) + scale.y * centerOffset.y);
    }

    public void update(float delta) {}

    public void render(SpriteBatch batch, OrthographicCamera camera) {}

    public void delete() {
        constraints.clear();
    }

    public GuiTransform set(String x, String y, String width, String height) {
        setPosition(x, y);
        setScale(width, height);
        return this;
    }

    public Vector2 getPosition() {
        return new Vector2(position);
    }

    public Vector2 getScale() {
        return new Vector2(scale);
    }

    public GuiTransform setPosition(int x, int y) {
        this.position.set(x, y);
        return this;
    }

    public GuiTransform setPosition(Vector2 position) {
        return setPosition((int) position.x, (int) position.y);
    }

    public GuiTransform setPosition(String x, String y) {
        addConstraint(Axis.X, getConstraintFromString(x));
        addConstraint(Axis.Y, getConstraintFromString(y));
        return this;
    }

    public GuiTransform setScale(int width, int height) {
        this.scale.set(width, height);
        return this;
    }

    public GuiTransform setScale(Vector2 scale) {
        return setScale((int) scale.x, (int) scale.y);
    }

    public GuiTransform setScale(String width, String height) {
        addConstraint(Axis.WIDTH, getConstraintFromString(width));
        addConstraint(Axis.HEIGHT, getConstraintFromString(height));
        return this;
    }

    public GuiTransform setOffset(String x, String y) {
        setOffsetX(x); setOffsetY(y);
        return this;
    }

    public GuiTransform setOffsetX(String x) {
        addConstraint(Axis.OFFSET_X, getOffsetFromString(x));
        return this;
    }

    public GuiTransform setOffsetY(String y) {
        addConstraint(Axis.OFFSET_Y, getOffsetFromString(y));
        return this;
    }

    public GuiTransform setCentered() {
        setCenteredX(); setCenteredY();
        return this;
    }

    public GuiTransform setCentered(float x, float y) {
        setCenteredX(x); setCenteredY(y);
        return this;
    }

    public GuiTransform setCenteredX() {
        this.centerOffset.x = -0.5f;
        return this;
    }

    public GuiTransform setCenteredX(float centerX) {
        this.centerOffset.x = centerX;
        return this;
    }

    public GuiTransform setCenteredY() {
        this.centerOffset.y = -0.5f;
        return this;
    }

    public GuiTransform setCenteredY(float centerY) {
        this.centerOffset.y = centerY;
        return this;
    }

    public GuiTransform setValue(Axis axis, float value) {
        constraints.get(axis).setValue(value);
        return this;
    }

    public float getBaseValue(Axis axis) {
        if(!constraints.containsKey(axis)) return 0;
        return constraints.get(axis).getValue();
    }

    public int getValue(Axis axis) {
        switch(axis) {
            case X: return (int) position.x;
            case Y: return (int) position.y;
            case WIDTH: return (int) scale.x;
            case HEIGHT: return (int) scale.y;
        }
        return 0;
    }

    public GuiTransform initScreen(int width, int height) {
        this.parent = this;
        setPosition(0,0);
        setScale(width, height);
        return this;
    }

    public boolean isClicked(OrthographicCamera camera) {
        Vector2 coords = Tools.getWorldCoordsOfMouse(camera);
        return (coords.x >= position.x && coords.x <= position.x + scale.x) && (coords.y >= position.y && coords.y <= position.y + scale.y);
    }

    public GuiTransform setGuiScale(float guiScale) {
        this.baseGuiScale = guiScale;
        return this;
    }

    public GuiTransform setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean isVisible() { return visible; }

    private void addConstraint(Axis axis, Constraint constraint) {
        this.constraints.put(axis, constraint.setAxis(axis).setGui(this));
    }

    private Constraint getOffsetFromString(String offset) {
        char c = getChar(offset);
        float f = getFloatFromString(offset);
        if (c == 'r') return new RelativeOffset(f);
        return new PixelOffset(f);
    }

    private Constraint getConstraintFromString(String constraint) {
        char c = getChar(constraint);
        float f = getFloatFromString(constraint);
        switch(c) {
            case 'r': return new RelativeConstraint(f);
            case 'c': return new ConstantConstraint(f);
            default: return new PixelConstraint(f);
        }
    }

    private char getChar(String s) { return s.charAt(0); }
    private float getFloatFromString(String s) { return Float.parseFloat(s.substring(1)); }

    private int getConstraint(Axis axis) {
        if(!constraints.containsKey(axis)) return 0;
        return constraints.get(axis).getValue(guiScale);
    }

}
