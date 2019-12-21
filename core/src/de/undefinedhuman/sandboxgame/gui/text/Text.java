package de.undefinedhuman.sandboxgame.gui.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.ressources.font.Font;
import de.undefinedhuman.sandboxgame.engine.ressources.font.FontManager;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.GuiManager;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;

public class Text {

    public Vector2 position = new Vector2(), relativePosition = new Vector2(), positionOffset = new Vector2(), centerOffset = new Vector2();
    public Gui parent = GuiManager.instance.screen;

    private GlyphLayout layout = new GlyphLayout();
    private float maxLineLength, relativeLineLength, lineLength, baseTextScale = 1;
    private String text;
    private int align;
    private Font fontType;
    private BitmapFont font;
    private boolean wrap, usesRelativeLineLength;
    private Color color;
    private int textScale = 1;

    public Text() {
        this(Font.Normal,"", Color.WHITE,0,0,0, Align.left,false);
    }

    public Text(String text) {
        this(Font.Normal,text, Color.WHITE,0,0,0, Align.left,false);
    }

    public Text(Font font, Object text) {
        this(font, text, Color.WHITE,0,0,0, Align.left,false);
    }

    public Text(Font font, Object text, Color color) {
        this(font, text, color,0,0,0, Align.left,false);
    }

    public Text(Font font, Object text, Color color, float x, float y) {
        this(font, text, color, x, y,0, Align.left,false);
    }

    public Text(Font font, Object text, Color color, float x, float y, float maxLineLength) {
        this(font, text, color, x, y, maxLineLength, Align.left,false);
    }

    public Text(Font font, Object text, Color color, float x, float y, float maxLineLength, int align, boolean wrap) {
        this.fontType = font;
        this.font = FontManager.instance.getFont(font); this.color = color; this.position.set(x, y); this.text = String.valueOf(text);
        this.wrap = wrap; this.maxLineLength = maxLineLength; this.align = align;
        setLayout();
    }

    public void resize(int width, int height) {
        textScale = Math.max((int) Math.ceil(Main.guiScale * baseTextScale), 1);
        font = FontManager.instance.getFont(fontType, textScale);
        setLayout();
        if(usesRelativeLineLength) this.maxLineLength = parent.getScale().x * relativeLineLength;
        else maxLineLength = lineLength * textScale;
        setLayout();
        position.set(parent.getPosition())
                .add((int) (parent.getScale().x * relativePosition.x), (int) (parent.getScale().y * relativePosition.y))
                .add((int) ((positionOffset.x * textScale) + layout.width * centerOffset.x),(int) ((positionOffset.y * textScale) + layout.height + layout.height * centerOffset.y));
    }

    public void render(SpriteBatch batch) {
        font.draw(batch, layout, position.x, position.y);
    }

    public Text setFont(Font font) {
        this.fontType = font;
        this.font = FontManager.instance.getFont(font, textScale);
        setLayout();
        return this;
    }

    public Text setText(Object text) {
        this.text = String.valueOf(text);
        setLayout();
        return this;
    }

    public Text setRelativeLineLength(float relativeLineLength) {
        this.relativeLineLength = relativeLineLength;
        this.maxLineLength = parent.getBound(Axis.WIDTH) * relativeLineLength;
        usesRelativeLineLength = true;
        setLayout();
        return this;
    }

    public Text setLineLength(int lineLength) {
        this.lineLength = lineLength;
        this.maxLineLength = lineLength * textScale;
        usesRelativeLineLength = false;
        setLayout();
        return this;
    }

    public Text setColor(Color color) {
        this.color = color;
        setLayout();
        return this;
    }

    public Text setColor(float r, float g, float b, float a) {
        this.color.set(r, g, b, a);
        setLayout();
        return this;
    }

    public Text setWrap(boolean wrap) {
        this.wrap = wrap;
        setLayout();
        return this;
    }

    public Text setAlign(int align) {
        this.align = align;
        setLayout();
        return this;
    }

    public Text setTextScale(float baseTextScale) {
        this.baseTextScale = baseTextScale;
        return this;
    }

    public Text addCentered(float centerX, float centerY) {
        this.centerOffset.add(centerX, centerY);
        return this;
    }

    public Text setCentered() {
        this.centerOffset.set(-0.5f, -0.5f);
        return this;
    }

    public Text setCentered(float centerX, float centerY) {
        this.centerOffset.set(centerX, centerY);
        return this;
    }

    public Text setCenteredX(float centerX) {
        this.centerOffset.x = centerX;
        return this;
    }

    public Text setPositionOffset(Vector2 positionOffset) {
        this.positionOffset.set(positionOffset);
        return this;
    }

    public Text setRelativePosition(float x, float y) {
        relativePosition.set(x, y);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

    public Text setPositionOffset(float x, float y) {
        this.positionOffset.set(x, y);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

    public Text setPosition(int x, int y) {
        this.position.set(x, y);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

    public Text setPosition(Vector2 position) {
        this.position.set(position);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

    public Vector2 getScale() { return new Vector2(layout.width, layout.height); }
    public Vector2 getPosition() { return position; }
    public float getBaseTextScale() { return baseTextScale; }
    public Vector2 getPositionOffset() { return positionOffset; }
    public BitmapFont getFont() { return this.font; }
    public Color getColor() { return color; }
    public String getText() { return this.text; }
    public GlyphLayout getLayout() { return layout; }
    public void delete() { layout.reset(); }

    private void setLayout() {
        layout.setText(font, text, color, maxLineLength, (int) align, wrap);
    }

}