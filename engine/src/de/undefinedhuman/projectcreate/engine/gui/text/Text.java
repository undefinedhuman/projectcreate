package de.undefinedhuman.projectcreate.engine.gui.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import de.undefinedhuman.projectcreate.engine.gui.GuiComponent;
import de.undefinedhuman.projectcreate.engine.gui.GuiManager;
import de.undefinedhuman.projectcreate.engine.gui.transforms.Axis;
import de.undefinedhuman.projectcreate.engine.gui.transforms.GuiTransform;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.Constraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.TextConstraint;
import de.undefinedhuman.projectcreate.engine.resources.font.Font;
import de.undefinedhuman.projectcreate.engine.resources.font.FontManager;

public class Text extends GuiComponent {

    private GlyphLayout layout = new GlyphLayout();
    private String text, truncate = "...";
    private int align = Align.left;
    private Font fontType = Font.Normal;
    private BitmapFont font;
    private boolean wrap = false;
    private Constraint lineLength;
    private int fontSize = 16;

    public Text(Object text) {
        super();
        this.text = String.valueOf(text);
        this.font = FontManager.getInstance().getFont(fontType, GuiManager.GUI_SCALE * fontSize);
        setSize(new TextConstraint(layout), new TextConstraint(layout));
        lineLength = new PixelConstraint(0).setGui(this).setAxis(Axis.WIDTH);
    }

    @Override
    public void resize(int width, int height) {
        font = FontManager.getInstance().getFont(fontType, GuiManager.GUI_SCALE * fontSize);
        int lineLength = this.lineLength.getValue(GuiManager.GUI_SCALE);
        layout.setText(font, text, 0, text.length(), color, lineLength, align, wrap, wrap || lineLength == 0 ? null : truncate);
        super.resize(width, height);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        if (!visible || !parent.isVisible()) return;
        font.draw(batch, layout, getCurrentValue(Axis.X), getCurrentValue(Axis.Y) + getCurrentValue(Axis.HEIGHT));
    }

    @Override
    public void delete() {
        super.delete();
        layout.reset();
    }

    @Override
    public GuiTransform set(Constraint x, Constraint y, Constraint width, Constraint height) {
        return super.set(x, y, new TextConstraint(layout), new TextConstraint(layout));
    }

    @Override
    public GuiTransform setSize(Constraint width, Constraint height) {
        return super.setSize(new TextConstraint(layout), new TextConstraint(layout));
    }

    public Text setText(Object text) {
        this.text = String.valueOf(text);
        resize();
        return this;
    }

    public Text setFont(Font font) {
        this.fontType = font;
        resize();
        return this;
    }

    public Text setLineLength(Constraint lineLength) {
        this.lineLength = lineLength
                .setAxis(Axis.WIDTH)
                .setGui(this);
        resize();
        return this;
    }

    public Text setWrap(boolean wrap) {
        this.wrap = wrap;
        resize();
        return this;
    }

    public Text setAlign(int align) {
        this.align = align;
        resize();
        return this;
    }

    public Text setFontSize(int fontSize) {
        this.fontSize = fontSize;
        resize();
        return this;
    }

    public Text setTruncate(String truncate) {
        this.truncate = truncate;
        resize();
        return this;
    }

    public BitmapFont getFont() { return this.font; }

    public Color getColor() { return color; }

    public String getText() { return this.text; }

    public GlyphLayout getLayout() { return layout; }

}