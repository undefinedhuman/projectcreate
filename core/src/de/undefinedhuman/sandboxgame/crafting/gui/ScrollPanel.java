package de.undefinedhuman.sandboxgame.crafting.gui;

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

public class ScrollPanel extends Gui {

    private int offset = 0;

    private Gui scrollBar;

    private Rectangle scissors = new Rectangle(), clipBounds = new Rectangle();

    private Gui viewport;
    private Gui[] recipes = new Gui[10];

    private int maxOffset = 0;

    public ScrollPanel() {
        super(GuiTemplate.HOTBAR);
        scrollBar = new Gui(GuiTemplate.SCROLL_BAR);
        scrollBar.set(new RelativeConstraint(1), new PixelConstraint(0), new PixelConstraint(10), new RelativeConstraint(1));
        scrollBar.setOffsetX(new RelativeOffset(-1));

        viewport = new Gui(new GuiTexture()) {
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
        };
        // TEMP HEIGHT MAKE IT FLEXIBLE
        viewport.set(new PixelConstraint(0), new PixelConstraint(0), new RelativePixelConstraint(1, 12), new RelativeConstraint(1));

        for(int i = 0; i < recipes.length; i++) {
            Gui recipe = new Gui(GuiTemplate.SLOT);
            recipe.set(new RelativeConstraint(0), new RelativeConstraint(1), new RelativeConstraint(1), new PixelConstraint(Variables.SLOT_SIZE));
            recipe.setOffsetY(new PixelOffset(-(Variables.SLOT_SIZE + Variables.SLOT_SPACE) * (i+1) + Variables.SLOT_SPACE));
            recipes[i] = recipe;
            viewport.addChild(recipe);
        }

        addChild(viewport, scrollBar);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        maxOffset = recipes.length * (Variables.SLOT_SIZE + Variables.SLOT_SPACE) - Variables.SLOT_SPACE - viewport.getCurrentValue(Axis.HEIGHT) / Main.guiScale;
    }

    @Override
    public void init() {
        super.init();
    }

    public void scroll(int amount) {
        if(amount == 0) return;
        offset += amount * 10;
        offset = Tools.clamp(offset, 0, maxOffset);
        for(int i = 0; i < recipes.length; i++)
            recipes[i].setValue(Axis.OFFSET_Y, offset + -(Variables.SLOT_SIZE + Variables.SLOT_SPACE) * (i+1) + Variables.SLOT_SPACE).resize();
    }

}
