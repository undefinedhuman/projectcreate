package de.undefinedhuman.sandboxgame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.ressources.font.Font;
import de.undefinedhuman.sandboxgame.gui.text.Text;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTexture;
import de.undefinedhuman.sandboxgame.gui.transforms.GuiTransform;

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
        this.texture.resize((int) position.x, (int) position.y, (int) scale.x, (int) scale.y, guiScale);
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
        if (!visible || !parent.isVisible()) return;
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

    public void setTitle(GuiTemplate template, String titleString, Font font, Color color) {
        Gui title = new Gui(template);
        Text text = new Text(titleString);
        text.setFont(font).setColor(color).setPosition("r0.5", "r0.5").setCentered();
        title.addChild(text);
        title.set("r0.5", "r1", "p" + (text.getScale().x + template.cornerSize * 4), "p" + (text.getScale().y + template.cornerSize * 4)).setOffsetY("p-7").setCentered();
        addChild(title);
    }

    public Gui addChild(GuiTransform... components) {
        for (GuiTransform component : components) {
            component.parent = this;
            component.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.children.add(component);
        }
        return this;
    }

    @Override
    public GuiTransform setPosition(int x, int y) {
        texture.resize(x, y, (int) scale.x, (int) scale.y, guiScale);
        return super.setPosition(x, y);
    }

    @Override
    public GuiTransform setScale(int width, int height) {
        texture.resize((int) position.x, (int) position.y, width, height, guiScale);
        return super.setScale(width, height);
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
