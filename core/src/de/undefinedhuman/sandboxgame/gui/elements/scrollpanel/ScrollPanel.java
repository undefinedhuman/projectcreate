package de.undefinedhuman.sandboxgame.gui.elements.scrollpanel;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTexture;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativePixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.PixelOffset;

import java.util.ArrayList;

public class ScrollPanel extends Gui {

    private Gui viewport;
    private ScrollBar scrollBar;
    private ArrayList<Gui> content = new ArrayList<>();
    private float offset, maxOffset, contentHeight;
    private Rectangle scissors = new Rectangle(), clipBounds = new Rectangle();

    public ScrollPanel() {
        super(GuiTemplate.HOTBAR);

        scrollBar = new ScrollBar(GuiTemplate.SCROLL_BAR, 10).addChangeListener(progress -> {
            if(maxOffset > 0)
                updateOffset(Tools.clamp(progress * Math.max(maxOffset * (1f / 0.9f), 1f), 0, maxOffset));
        });

        viewport = (Gui) new Gui(new GuiTexture()) {
            @Override
            public void resize(int width, int height) {
                super.resize(width, height);
                scissors.set(0, 0, 0, 0);
                clipBounds.set(viewport.getCurrentValue(Axis.X), viewport.getCurrentValue(Axis.Y), viewport.getCurrentValue(Axis.WIDTH), viewport.getCurrentValue(Axis.HEIGHT));
            }

            @Override
            public void render(SpriteBatch batch, OrthographicCamera camera) {
                batch.flush();
                ScissorStack.calculateScissors(camera, batch.getTransformMatrix(), clipBounds, scissors);
                ScissorStack.pushScissors(scissors);
                super.render(batch, camera);
                batch.flush();
                ScissorStack.popScissors();
            }
        }.set(new PixelConstraint(0), new PixelConstraint(0), new RelativePixelConstraint(1, 12), new RelativeConstraint(1));

        addChild(viewport, scrollBar);
    }

    @Override
    public void init() {
        super.init();
        initContent();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        float viewportHeight = viewport.getCurrentValue(Axis.HEIGHT);
        maxOffset = (contentHeight * Main.guiScale - viewportHeight) / viewportHeight;
    }

    @Override
    public void delete() {
        super.delete();
        content.clear();
    }

    public void clear() {
        this.content.clear();
        this.viewport.clearChildren();
    }

    public void scroll(int amount) {
        if(amount == 0 || maxOffset < 0)
            return;
        updateOffset(Tools.clamp(offset - (float) (amount * Variables.MOUSE_SENSITIVITY) / scrollBar.getScrollBarHeight(), 0, maxOffset));
    }

    public void setContent(int contentHeight, int contentSpacing, Gui... content) {
        clear();
        for(int i = 0; i < content.length; i++) {
            content[i]
                    .set(new PixelConstraint(0), new RelativeConstraint(0), new RelativeConstraint(1), new PixelConstraint(contentHeight))
                    .setOffsetY(new PixelOffset((contentHeight + contentSpacing) * i));
            this.content.add(content[i]);
            viewport.addChild(content[i]);
        }
        this.contentHeight = (content.length * (contentHeight + contentSpacing) - contentSpacing);
        resize();
        initContent();
    }

    private void updateOffset(float offset) {
        scrollBar.updateThumbY(Tools.clamp((offset / Math.max(maxOffset, 0.9f)) * 0.9f, 0, 0.9f));

        for (Gui gui : content)
            gui.setValue(Axis.Y, -offset).resize();
        this.offset = offset;
    }

    private void initContent() {
        boolean visible = maxOffset > 0;
        scrollBar.setVisible(visible);
        ((RelativePixelConstraint) viewport.getConstraint(Axis.WIDTH)).setOffset(visible ? 12 : 0);
        viewport.resize();
        scrollBar.updateThumbHeight(1f - maxOffset);
        updateOffset(maxOffset);
    }

}
