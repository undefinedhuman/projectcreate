package de.undefinedhuman.sandboxgame.gui.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.resources.font.Font;
import de.undefinedhuman.sandboxgame.engine.resources.font.FontManager;
import de.undefinedhuman.sandboxgame.gui.GuiComponent;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraint;
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
        this.font = FontManager.instance.getFont(fontType, Main.guiScale);
        lineLength = new PixelConstraint(0).setGui(this).setAxis(Axis.WIDTH);
        calcScale = false;
    }

    public Text setWrap(boolean wrap) {
        this.wrap = wrap;
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

    @Override
    public void resize(int width, int height) {
        guiScale = Math.max((int) Math.ceil(Main.guiScale * baseGuiScale), 1);
        font = FontManager.instance.getFont(fontType, guiScale);
        layout.setText(font, text, color, lineLength.getValue(guiScale), align, wrap);
        setScale((int) layout.width, (int) layout.height);
        super.resize(width, height);
        position.y += layout.height;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        if (!visible || !parent.isVisible()) return;
        font.draw(batch, layout, position.x, position.y);
    }

    @Override
    public void delete() {
        super.delete();
        layout.reset();
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

    public Text setFont(Font font) {
        this.fontType = font;
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

    public Color getColor() { return color; }

    public String getText() { return this.text; }

    public Text setText(Object text) {
        this.text = String.valueOf(text);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

    public GlyphLayout getLayout() { return layout; }

}