package de.undefinedhuman.sandboxgame.gui.elements.scrollpanel;

import com.badlogic.gdx.Gdx;
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
import de.undefinedhuman.sandboxgame.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.sandboxgame.utils.Mouse;

import java.util.ArrayList;

public class ScrollPanel extends Gui {

    private Gui viewport, scrollBar, thumb;
    private ArrayList<Gui> content = new ArrayList<>();
    private float mouseOffset = 0, offset, maxOffset, scrollBarY, scrollBarHeight;
    private boolean grabbed = false;
    private Rectangle scissors = new Rectangle(), clipBounds = new Rectangle();

    public ScrollPanel() {
        super(GuiTemplate.HOTBAR);
        scrollBar = (Gui) new Gui(GuiTemplate.SCROLL_BAR)
                .addChild(
                        thumb = (Gui) new Gui(GuiTemplate.SLOT)
                                .set(new RelativeConstraint(0), new RelativeConstraint(0), new RelativeConstraint(1), new RelativeConstraint(1))
                )
                .set(new RelativeConstraint(1), new PixelConstraint(0), new PixelConstraint(10), new RelativeConstraint(1))
                .setOffsetX(new RelativeOffset(-1f));

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

    private void initContent() {
        thumb.setValue(Axis.HEIGHT, Tools.clamp(1f - maxOffset, 0.1f, 1f));
        updateOffset(maxOffset);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        scrollBarY = scrollBar.getCurrentValue(Axis.Y) + scrollBar.getCornerSize();
        scrollBarHeight = scrollBar.getCurrentValue(Axis.HEIGHT) - scrollBar.getCornerSize() * 2;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        if (thumb.isClicked(camera) && Mouse.isLeftClicked() && !grabbed) {
            grabbed = true;
            mouseOffset = (Gdx.graphics.getHeight() - Mouse.getY()) - thumb.getCurrentValue(Axis.Y);
        } else if (!Mouse.isLeftClicked()) grabbed = false;

        if (grabbed && maxOffset > 0)
            updateOffset(Tools.clamp((Gdx.graphics.getHeight() - Mouse.getY() - mouseOffset - scrollBarY) / scrollBarHeight * Math.max(maxOffset * (1f / 0.9f), 1f), 0, maxOffset));
    }

    @Override
    public void delete() {
        super.delete();
        viewport.deleteChildren();
        content.clear();
    }

    public void clear() {
        this.content.clear();
        this.viewport.clearChildren();
    }

    public void scroll(int amount) {
        if(amount == 0 || maxOffset < 0)
            return;
        updateOffset(Tools.clamp(offset - (float) (amount * Variables.MOUSE_SENSITIVITY) / scrollBarHeight, 0, maxOffset));
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
        float viewportHeight = viewport.getCurrentValue(Axis.HEIGHT);
        maxOffset = ((content.length * (contentHeight + contentSpacing) - contentSpacing) * Main.guiScale - viewportHeight) / viewportHeight;
        resize();
        initContent();
    }

    private void updateOffset(float offset) {
        thumb
                .setValue(Axis.Y, Tools.clamp((offset / Math.max(maxOffset, 0.9f)) * 0.9f, 0, 0.9f))
                .resize();

        for (Gui gui : content)
            gui.setValue(Axis.Y, -offset).resize();
        this.offset = offset;
    }

}
