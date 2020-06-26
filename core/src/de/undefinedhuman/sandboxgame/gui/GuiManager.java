package de.undefinedhuman.sandboxgame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.engine.resources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.GuiTransform;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.ResizeConstraint;

import java.util.ArrayList;

public class GuiManager extends Manager {

    public static GuiManager instance;
    public GuiTransform screen = null;

    private ArrayList<GuiTransform> guiTransforms = new ArrayList<>();

    public GuiManager() {
        if (instance == null) instance = this;
    }

    @Override
    public void init() {
        for (GuiTemplate template : GuiTemplate.values()) template.load();
        TextureManager.instance.addTexture("gui/Chains/Chain-Top-Right.png", "gui/Chains/Chain-Top-Left.png", "gui/Chains/Chain-Mid-Right.png", "gui/Chains/Chain-Mid-Left.png", "gui/Chains/Chain-Bottom-Right.png", "gui/Chains/Chain-Bottom-Left.png");
        screen = new GuiComponent().set(new PixelConstraint(0), new PixelConstraint(0), new ResizeConstraint(), new ResizeConstraint()).initScreen();
    }

    @Override
    public void resize(int width, int height) {
        for (GuiTransform gui : guiTransforms) gui.resize(width, height);
    }

    @Override
    public void update(float delta) {
        for (GuiTransform gui : guiTransforms) gui.update(delta);
    }

    @Override
    public void renderGui(SpriteBatch batch, OrthographicCamera camera) {
        for (GuiTransform gui : guiTransforms) gui.render(batch, camera);
    }

    @Override
    public void delete() {
        for (GuiTemplate template : GuiTemplate.values()) template.delete();
        screen.delete();
        guiTransforms.clear();
    }

    public boolean hasGui(GuiTransform gui) { return guiTransforms.contains(gui); }

    public void addGui(GuiTransform... guiTransforms) {
        for (GuiTransform guiTransform : guiTransforms) {
            if (hasGui(guiTransform)) continue;
            this.guiTransforms.add(guiTransform);
            guiTransform.init();
            guiTransform.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    public void removeGui(GuiTransform gui) { if (hasGui(gui)) this.guiTransforms.remove(gui); }

}
