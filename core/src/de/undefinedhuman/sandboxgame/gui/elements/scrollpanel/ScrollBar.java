package de.undefinedhuman.sandboxgame.gui.elements.scrollpanel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.event.ChangeListener;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.sandboxgame.utils.Mouse;

import java.util.ArrayList;

public class ScrollBar extends Gui {

    private Gui thumb;
    private boolean grabbed = false;
    private float mouseOffset, scrollBarY, scrollBarHeight;

    private ArrayList<ChangeListener> changeListeners = new ArrayList<>();

    public ScrollBar(GuiTemplate template, int width) {
        super(template);
        set(new RelativeConstraint(1), new PixelConstraint(0), new PixelConstraint(width), new RelativeConstraint(1));
        setOffsetX(new RelativeOffset(-1f));

        addChild(thumb = (Gui) new Gui(GuiTemplate.SLOT)
                .set(new RelativeConstraint(0), new RelativeConstraint(0), new RelativeConstraint(1), new RelativeConstraint(1))
        );
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        scrollBarY = getCurrentValue(Axis.Y) + getCornerSize();
        scrollBarHeight = getCurrentValue(Axis.HEIGHT) - getCornerSize() * 2;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        if (thumb.isClicked(camera) && Mouse.isLeftClicked() && !grabbed) {
            grabbed = true;
            mouseOffset = (Gdx.graphics.getHeight() - Mouse.getY()) - thumb.getCurrentValue(Axis.Y);
        } else if (!Mouse.isLeftClicked()) grabbed = false;

        if (grabbed)
            notifyChangeListener((Gdx.graphics.getHeight() - Mouse.getY() - mouseOffset - scrollBarY) / scrollBarHeight);
    }

    public void updateThumbHeight(float height) {
        this.thumb
                .setValue(Axis.HEIGHT, Tools.clamp(height, 0.1f, 1f))
                .resize();
    }

    public void updateThumbY(float y) {
        this.thumb
                .setValue(Axis.Y, y)
                .resize();
    }

    public float getScrollBarHeight() {
        return scrollBarHeight;
    }

    public ScrollBar addChangeListener(ChangeListener changeListener) {
        this.changeListeners.add(changeListener);
        return this;
    }

    private void notifyChangeListener(float offset) {
        for (ChangeListener changeListener : changeListeners)
            changeListener.notify(offset);
    }

}
