package de.undefinedhuman.sandboxgame.crafting.gui;

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

public class ScrollPanel extends Gui {

    private Gui viewport, scrollBar, thumb;
    private Gui[] recipes = new Gui[17];
    private int viewportHeight = 0;
    private float offset = 0, maxOffset;
    private boolean grabbed = false;
    private Rectangle scissors = new Rectangle(), clipBounds = new Rectangle();

    public ScrollPanel() {
        super(GuiTemplate.HOTBAR);
        scrollBar = (Gui) new Gui(GuiTemplate.SCROLL_BAR)
                .addChild(
                        thumb = (Gui) new Gui(GuiTemplate.SLOT)
                                .set(new PixelConstraint(0), new RelativeConstraint(0), new RelativeConstraint(1), new RelativeConstraint(1))
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

        for(int i = 0; i < recipes.length; i++) {
            Gui recipe = new Gui(GuiTemplate.SLOT);
            recipe.set(new PixelConstraint(0), new RelativeConstraint(0), new RelativeConstraint(1), new PixelConstraint(Variables.SLOT_SIZE));
            recipe.setOffsetY(new PixelOffset((Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i));
            recipes[i] = recipe;
            viewport.addChild(recipe);
        }

        addChild(viewport, scrollBar);
    }

    @Override
    public void init() {
        super.init();
        resize();
        thumb.setValue(Axis.Y, maxOffset).resize();
        for (Gui recipe : recipes)
            recipe.setValue(Axis.Y, -maxOffset).resize();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewportHeight = viewport.getCurrentValue(Axis.HEIGHT);
        maxOffset = (float) ((recipes.length * (Variables.SLOT_SIZE + Variables.SLOT_SPACE) - Variables.SLOT_SPACE) * Main.guiScale - viewportHeight) / viewportHeight;
        thumb.setValue(Axis.HEIGHT, Tools.clamp(1f - maxOffset, 0.1f, 1f)).resize();
    }

    private float mouseOffset = 0;

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        if (thumb.isClicked(camera) && Mouse.isLeftClicked() && !grabbed) {
            grabbed = true;
            mouseOffset = (Gdx.graphics.getHeight() - Mouse.getY()) - thumb.getCurrentValue(Axis.Y);
        } else if (!Mouse.isLeftClicked()) grabbed = false;

        if (grabbed) {
            float mouseY = (Gdx.graphics.getHeight() - Mouse.getY() - mouseOffset) - scrollBar.getCurrentValue(Axis.Y);
            this.offset = Tools.clamp(mouseY / scrollBar.getCurrentValue(Axis.HEIGHT), 0, maxOffset);
            thumb.setValue(Axis.Y, offset).resize();
        }
    }

    public void scroll(int amount) {
        if(amount == 0) return;
        offset -= (float) (amount * Variables.MOUSE_SENSITIVITY) / viewportHeight * Main.guiScale;
        offset = Tools.clamp(offset, 0, maxOffset);
        for (Gui recipe : recipes)
            recipe.setValue(Axis.Y, -offset).resize();
    }

}
