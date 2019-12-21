package de.undefinedhuman.sandboxgame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.gui.text.Text;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTexture;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.Bounds;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraints;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.Offset;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.PixelOffset;

import java.util.ArrayList;

public class Gui {

    public Gui parent;

    protected float localScale = 1f;
    protected Bounds bounds = new Bounds();
    protected GuiTexture texture;
    protected Color color = new Color(Color.WHITE);
    protected boolean visible = true;

    public Constraints constraints;
    private Offset xOffset, yOffset;
    private ArrayList<Gui> children = new ArrayList<>();
    private ArrayList<Text> texts = new ArrayList<>();

    public Gui(GuiTexture texture, Constraints constraints) {
        this.texture = texture;
        parent = GuiManager.instance.screen;
        this.xOffset = new PixelOffset(this, Axis.X,0);
        this.yOffset = new PixelOffset(this, Axis.Y,0);
        this.constraints = constraints.setGui(this);
    }

    public void resize() {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void resize(int width, int height) {
        float scale = Main.guiScale * localScale;
        this.texture.resize(scale);
        this.bounds.setBounds(constraints, xOffset, yOffset, scale);
        for (Gui child : children) child.resize(width, height);
        for (Text text : texts) text.resize(width, height);
    }

    public void update(float delta) {
        for(Gui child : children) child.update(delta);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if(!visible || !parent.visible) return;
        texture.render(batch, bounds.getValue(Axis.X), bounds.getValue(Axis.Y), bounds.getValue(Axis.WIDTH), bounds.getValue(Axis.HEIGHT), color);
        for(Gui child : children) child.render(batch, camera);
        for(Text text : texts) text.render(batch);
    }

    public Gui addChild(Gui... children) {
        for(Gui child : children) {
            child.parent = this;
            child.resize();
            this.children.add(child);
        }
        return this;
    }

    public Gui setTexture(GuiTexture texture) {
        if(texture != null) texture.delete();
        this.texture = texture;
        resize();
        return this;
    }

    public Gui setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public Gui setAlpha(float alpha) {
        this.color.a = alpha;
        return this;
    }

    public Gui setColor(Color color) {
        float alpha = this.color.a;
        this.color = new Color(color.r, color.g, color.b, alpha);
        return this;
    }

    public Gui setGuiScale(float scale) {
        this.localScale = scale;
        resize();
        return this;
    }

    public void setOffsetX(float x) {
        this.xOffset.setValue(x);
        resize();
    }

    public void setOffsetY(float y) {
        this.yOffset.setValue(y);
        resize();
    }

    public Gui setOffsetX(Offset offset) {
        this.xOffset = offset;
        resize();
        return this;
    }

    public Gui setOffsetY(Offset offset) {
        this.yOffset = offset;
        resize();
        return this;
    }

    public Gui setOffsets(Offset x, Offset y) {
        this.xOffset = x;
        this.yOffset = y;
        resize();
        return this;
    }

    public Vector2 getPosition() {
        return new Vector2(bounds.getValue(Axis.X), bounds.getValue(Axis.Y));
    }

    public Vector2 getScale() {
        return new Vector2(bounds.getValue(Axis.WIDTH), bounds.getValue(Axis.HEIGHT));
    }

    public int getBound(Axis axis) {
        return bounds.getValue(axis);
    }

    public Constraints getConstraints() {
        return constraints;
    }

    public Gui initScreen(float width, float height) {
        this.parent = this;
        this.bounds.initScreen(width, height);
        return this;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void delete() {
        for(Gui child : children) child.delete();
        for(Text text : texts) text.delete();
        constraints.delete();
        children.clear();
        texture.delete();
        texts.clear();
    }

}
