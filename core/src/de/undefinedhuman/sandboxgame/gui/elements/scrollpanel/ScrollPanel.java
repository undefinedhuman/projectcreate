package de.undefinedhuman.sandboxgame.gui.elements.scrollpanel;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.event.ChangeListener;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTexture;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.PixelOffset;

import java.util.ArrayList;

public class ScrollPanel<T extends Gui> extends Gui {

    private Gui viewport;
    private ScrollBar scrollBar;
    private ArrayList<T> content = new ArrayList<>();
    private float offset, maxOffset;
    private Rectangle scissors = new Rectangle(), clipBounds = new Rectangle();

    public ScrollPanel(GuiTemplate template) {
        super(template);
        scrollBar = (ScrollBar) new ScrollBar(GuiTemplate.SCROLL_BAR, 10).addListener((ChangeListener) progress -> {
            if (maxOffset > 0)
                updateOffset(Tools.clamp(Math.max(maxOffset * (1f / 0.9f), 1f) * progress, 0, maxOffset));
        });

        viewport = (Gui) new Gui(new GuiTexture())
                .set(new PixelConstraint(0), new PixelConstraint(0), new RelativeConstraint(1, -12), new RelativeConstraint(1));

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
        for(T gui : content)
            gui.resize(width, height);
        float viewportHeight = viewport.getCurrentValue(Axis.HEIGHT);
        maxOffset = (calculateContentHeight() - viewportHeight) / viewportHeight;
        scissors.set(0, 0, 0, 0);
        clipBounds.set(viewport.getCurrentValue(Axis.X), viewport.getCurrentValue(Axis.Y), viewport.getCurrentValue(Axis.WIDTH), viewport.getCurrentValue(Axis.HEIGHT));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for(T gui : content)
            gui.update(delta);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        batch.flush();
        ScissorStack.calculateScissors(camera, batch.getTransformMatrix(), clipBounds, scissors);
        ScissorStack.pushScissors(scissors);
        for(T gui : content)
            gui.render(batch, camera);
        batch.flush();
        ScissorStack.popScissors();
    }

    @Override
    public void delete() {
        super.delete();
        clear();
    }

    public void clear() {
        for(T gui : content)
            gui.delete();
        this.content.clear();
    }

    public void scroll(int amount) {
        if(amount == 0 || maxOffset < 0)
            return;
        updateOffset(Tools.clamp(offset - (float) (amount * Variables.MOUSE_SENSITIVITY) / scrollBar.getScrollBarHeight(), 0, maxOffset));
    }

    public void addContent(T gui) {
        gui.setOffsetY(new PixelOffset(calculateContentHeight() / Main.guiScale + (content.size() > 0 ? Variables.SLOT_SPACE : 0)));
        gui.parent = viewport;
        this.content.add(gui);
        resize();
        initContent();
    }

    private void initContent() {
        boolean visible = maxOffset > 0;
        scrollBar.setVisible(visible);
        ((RelativeConstraint) viewport.getConstraint(Axis.WIDTH)).setOffset(visible ? 12 : 0);
        viewport.resize();
        scrollBar.updateThumbHeight(1f - maxOffset);
        updateOffset(maxOffset);
    }

    private void updateOffset(float offset) {
        scrollBar.updateThumbY(Tools.clamp((offset / Math.max(maxOffset, 0.9f)) * 0.9f, 0, 0.9f));

        for (Gui gui : content)
            gui.setValue(Axis.Y, -offset).resize();
        this.offset = offset;
    }

    private int calculateContentHeight() {
        if(content.size() == 0)
            return 0;
        if(content.size() == 1)
            return content.get(0).getCurrentValue(Axis.HEIGHT);
        Gui lastItem = content.get(content.size()-1);
        return lastItem.getCurrentValue(Axis.Y) + lastItem.getCurrentValue(Axis.HEIGHT) - content.get(0).getCurrentValue(Axis.Y);
    }

}