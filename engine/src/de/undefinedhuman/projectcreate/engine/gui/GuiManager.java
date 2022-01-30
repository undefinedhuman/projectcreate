package de.undefinedhuman.projectcreate.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.engine.gui.transforms.GuiTransform;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.ScreenConstraint;

import java.util.ArrayList;

public class GuiManager implements Manager {

    public static int GUI_SCALE = 1;
    private static volatile GuiManager instance;

    public GuiTransform screen = null;
    private ArrayList<GuiTransform> guiTransforms = new ArrayList<>();

    private GuiManager() { }

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
        GUI_SCALE = (int) Math.max(Math.ceil(width/640f) / 2, 1);
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
        guiTransforms
                .forEach(guiTransform -> guiTransform.render(batch, camera));
    }

    @Override
    public void delete() {
        screen.delete();
        guiTransforms
                .forEach(GuiTransform::delete);
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

    public static GuiManager getInstance() {
        if (instance == null) {
            synchronized (GuiManager.class) {
                if (instance == null)
                    instance = new GuiManager();
            }
        }
        return instance;
    }

}
