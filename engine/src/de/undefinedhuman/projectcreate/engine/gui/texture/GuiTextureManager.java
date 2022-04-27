package de.undefinedhuman.projectcreate.engine.gui.texture;

import de.undefinedhuman.projectcreate.engine.gui.GuiManager;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;
import de.undefinedhuman.projectcreate.engine.utils.ds.Tuple;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;

import java.util.ArrayList;
import java.util.HashMap;

public class GuiTextureManager implements Manager {

    private static volatile GuiTextureManager instance;

    private final Tuple<String, Vector2i> TEMP_KEY = new Tuple<>("", new Vector2i());
    private HashMap<Tuple<String, Vector2i>, GuiTexture> guiTextures = new HashMap<>();
    private ArrayList<Tuple<String, Vector2i>> toBeRemoved = new ArrayList<>();
    private boolean resize = false;

    private GuiTextureManager() { }

    @Override
    public void resize(int width, int height) {
        toBeRemoved.addAll(guiTextures.keySet());
        resize = true;
    }

    @Override
    public void update(float delta) {
        if(!resize)
            return;
        for(Tuple<String, Vector2i> key : toBeRemoved) {
            guiTextures.get(key).delete();
            guiTextures.remove(key);
        }
        toBeRemoved.clear();
        resize = false;
    }

    @Override
    public void delete() {
        for(GuiTexture guiTexture : guiTextures.values())
            guiTexture.delete();
        guiTextures.clear();
        toBeRemoved.clear();
    }

    public GuiTextureManager addGuiTexture(String textureName, int width, int height) {
        if(hasGuiTexture(textureName, width, height)) {
            toBeRemoved.remove(TEMP_KEY);
            return this;
        }
        GuiTexture texture = new GuiTexture(textureName).init();
        texture.resize(width, height, GuiManager.GUI_SCALE);
        guiTextures.put(new Tuple<>(textureName, new Vector2i(width, height)), texture);
        return this;
    }

    public GuiTextureManager addGuiTexture(GuiTemplate template, int width, int height) {
        if(hasGuiTexture(template.templateName, width, height)) {
            toBeRemoved.remove(TEMP_KEY);
            return this;
        }
        GuiTexture texture = new GuiTexture(template).init();
        texture.resize(width, height, GuiManager.GUI_SCALE);
        guiTextures.put(new Tuple<>(template.templateName, new Vector2i(width, height)), texture);
        return this;
    }

    public GuiTexture getGuiTexture(String textureName, int width, int height) {
        if(hasGuiTexture(textureName, width, height))
            return guiTextures.get(TEMP_KEY);
        else return null;
    }

    public GuiTexture getGuiTexture(GuiTemplate template, int width, int height) {
        return this.getGuiTexture(template.templateName, width, height);
    }

    public boolean hasGuiTexture(String textureName, int width, int height) {
        setKey(textureName, width, height);
        return guiTextures.containsKey(TEMP_KEY);
    }

    private void setKey(String textureName, int width, int height) {
        this.TEMP_KEY.setT(textureName);
        this.TEMP_KEY.getU().set(width, height);
    }

    public static GuiTextureManager getInstance() {
        if (instance == null) {
            synchronized (GuiTextureManager.class) {
                if (instance == null)
                    instance = new GuiTextureManager();
            }
        }
        return instance;
    }

}
