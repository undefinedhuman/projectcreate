package de.undefinedhuman.sandboxgame.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTexture;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraints;
import de.undefinedhuman.sandboxgame.utils.Manager;

import java.util.ArrayList;

public class GuiManager extends Manager {

    public static GuiManager instance;
    public Gui screen = null;

    private ArrayList<Gui> guis = new ArrayList<>();

    public GuiManager() {
        if(instance != null) instance = this;
    }

    @Override
    public void init() {
        for(GuiTemplate template : GuiTemplate.values()) template.load();
        TextureManager.instance.addTexture("gui/Chains/Chain-Top-Right.png", "gui/Chains/Chain-Top-Left.png", "gui/Chains/Chain-Mid-Right.png", "gui/Chains/Chain-Mid-Left.png", "gui/Chains/Chain-Bottom-Right.png", "gui/Chains/Chain-Bottom-Left.png");
        screen = new Gui(new GuiTexture(), new Constraints()).initScreen(1280,720);
    }

    @Override
    public void resize(float width, float height) {
        screen.initScreen(width, height);
        for(Gui gui : guis) gui.resize((int) width, (int) height);
    }

    @Override
    public void update(float delta) {
        for(Gui gui : guis) gui.update(delta);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        for (Gui gui : guis) gui.render(batch, camera);
    }

    @Override
    public void delete() {
        for(GuiTemplate template : GuiTemplate.values()) template.delete();
        screen.delete();
        guis.clear();
    }

    public void addGui(Gui... guis) { for(Gui gui : guis) if(!hasGui(gui)) this.guis.add(gui); }
    public void removeGui(Gui gui) { if(hasGui(gui)) this.guis.remove(gui); }
    public boolean hasGui(Gui gui) { return guis.contains(gui); }

}
