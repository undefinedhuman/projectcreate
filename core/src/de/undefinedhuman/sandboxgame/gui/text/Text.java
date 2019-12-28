package de.undefinedhuman.sandboxgame.gui.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import de.undefinedhuman.sandboxgame.engine.ressources.font.Font;
import de.undefinedhuman.sandboxgame.engine.ressources.font.FontManager;
import de.undefinedhuman.sandboxgame.gui.GuiComponent;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.ConstantConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraints;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;

public class Text extends GuiComponent {

    private GlyphLayout layout = new GlyphLayout();
    private String text;
    private int align = Align.left;
    private Font fontType = Font.Normal;
    private BitmapFont font;
    private boolean wrap = false;
    private Constraint lineLength;

    public Text(Object text) {
        super();
        this.text = String.valueOf(text);
        this.font = FontManager.instance.getFont(fontType);
        lineLength = new PixelConstraint(0).setGui(this).setAxis(Axis.WIDTH);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        font = FontManager.instance.getFont(fontType, scale);
        layout.setText(font, text, color, lineLength.getValue(scale), align, wrap);
        this.constraints.setValue(Axis.WIDTH, layout.width);
        this.constraints.setValue(Axis.HEIGHT, layout.height);
        super.resize(width, height);
        bounds.addValue(Axis.Y, (int) layout.height);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        font.draw(batch, layout, bounds.getValue(Axis.X), bounds.getValue(Axis.Y));
    }

    @Override
    public void delete() {
        super.delete();
        layout.reset();
    }

    public Text setFont(Font font) {
        this.fontType = font;
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

    public Text setText(Object text) {
        this.text = String.valueOf(text);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

    public Text setWrap(boolean wrap) {
        this.wrap = wrap;
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

    public Text setAlign(int align) {
        this.align = align;
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

    public Text setLineLength(Constraint lineLength) {
        this.lineLength = lineLength.setAxis(Axis.WIDTH).setGui(this);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

    public BitmapFont getFont() { return this.font; }
    public Color getColor() { return color; }
    public String getText() { return this.text; }
    public GlyphLayout getLayout() { return layout; }

    @Override
    public GuiComponent setConstraints(Constraints constraints) {
        super.setConstraints(constraints);
        this.constraints.setConstraint(Axis.WIDTH, new ConstantConstraint(0));
        this.constraints.setConstraint(Axis.HEIGHT, new ConstantConstraint(0));
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

}