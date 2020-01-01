package de.undefinedhuman.sandboxgame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.GuiTransform;
import de.undefinedhuman.sandboxgame.utils.Manager;

import java.util.ArrayList;

public class GuiManager extends Manager {

    public static GuiManager instance;
    public GuiTransform screen = null;

    private ArrayList<GuiTransform> guis = new ArrayList<>();

    public GuiManager() {
        if(instance == null) instance = this;
    }

    @Override
    public void init() {
        for(GuiTemplate template : GuiTemplate.values()) template.load();
        TextureManager.instance.addTexture("gui/Chains/Chain-Top-Right.png", "gui/Chains/Chain-Top-Left.png", "gui/Chains/Chain-Mid-Right.png", "gui/Chains/Chain-Mid-Left.png", "gui/Chains/Chain-Bottom-Right.png", "gui/Chains/Chain-Bottom-Left.png");
        screen = new GuiComponent().initScreen(1280,720);
    }

    @Override
    public void resize(int width, int height) {
        screen.setScale(width, height);
        for(GuiTransform gui : guis) gui.resize(width, height);
    }

    @Override
    public void update(float delta) {
        for(GuiTransform gui : guis) gui.update(delta);
    }

    @Override
    public void renderGui(SpriteBatch batch, OrthographicCamera camera) {
        for (GuiTransform gui : guis) gui.render(batch, camera);
    }

    @Override
    public void delete() {
        for(GuiTemplate template : GuiTemplate.values()) template.delete();
        screen.delete();
        guis.clear();
    }

    public void addGui(GuiTransform... guis) {
        for(GuiTransform gui : guis) if(!hasGui(gui)) {
            this.guis.add(gui);
            gui.init();
            gui.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    public void removeGui(GuiTransform gui) { if(hasGui(gui)) this.guis.remove(gui); }
    public boolean hasGui(GuiTransform gui) { return guis.contains(gui); }

}
