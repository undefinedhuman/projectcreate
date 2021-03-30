package de.undefinedhuman.projectcreate.core.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.ScreenConstraint;
import de.undefinedhuman.projectcreate.core.engine.utils.Manager;
import de.undefinedhuman.projectcreate.core.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.core.gui.transforms.GuiTransform;

import java.util.ArrayList;

public class GuiManager extends Manager {

    public static GuiManager instance;
    public GuiTransform screen = null;

    private ArrayList<GuiTransform> guiTransforms = new ArrayList<>();

    public GuiManager() {
        if (instance == null)
            instance = this;
    }

    @Override
    public void init() {
        for (GuiTemplate template : GuiTemplate.values())
            template.load();
        screen = new GuiComponent()
                .set(new PixelConstraint(0), new PixelConstraint(0), new ScreenConstraint(), new ScreenConstraint())
                .initScreen();
    }

    @Override
    public void resize(int width, int height) {
        screen.resize(width, height);
        for (GuiTransform guiTransform : guiTransforms)
            guiTransform.resize(width, height);
    }

    @Override
    public void update(float delta) {
        for (GuiTransform guiTransform : guiTransforms)
            guiTransform.update(delta);
    }

    @Override
    public void renderGui(SpriteBatch batch, OrthographicCamera camera) {
        for (GuiTransform guiTransform : guiTransforms)
            guiTransform.render(batch, camera);
    }

    @Override
    public void delete() {
        for (GuiTemplate template : GuiTemplate.values())
            template.delete();
        screen.delete();
        for(GuiTransform guiTransform : guiTransforms)
            guiTransform.delete();
        guiTransforms.clear();
    }

    public boolean hasGui(GuiTransform gui) { return guiTransforms.contains(gui); }

    public void addGui(GuiTransform... guiTransforms) {
        for (GuiTransform guiTransform : guiTransforms) {
            if (hasGui(guiTransform))
                continue;
            this.guiTransforms.add(guiTransform);
            guiTransform.init();
            guiTransform.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    public void removeGui(GuiTransform gui) {
        if (hasGui(gui))
            this.guiTransforms.remove(gui);
    }

}
