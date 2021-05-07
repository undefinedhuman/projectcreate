package de.undefinedhuman.projectcreate.core.gui.texture;

import de.undefinedhuman.projectcreate.core.Main;
import de.undefinedhuman.projectcreate.engine.utils.Manager;
import de.undefinedhuman.projectcreate.engine.utils.ds.Key;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;

import java.util.ArrayList;
import java.util.HashMap;

public class GuiTextureManager extends Manager {

    public static GuiTextureManager instance;

    private ArrayList<Key<String, Vector2i>> toBeRemoved = new ArrayList<>();
    private Key<String, Vector2i> tempKey = new Key<>("", new Vector2i());
    private HashMap<Key<String, Vector2i>, GuiTexture> guiTextures = new HashMap<>();

    public GuiTextureManager() {
        if(instance == null)
             instance = this;
    }

    @Override
    public void resize(int width, int height) {
        for(Key<String, Vector2i> key : guiTextures.keySet()) {
            GuiTexture texture = guiTextures.get(key);
            if(!texture.remove)
                continue;
            texture.delete();
            toBeRemoved.add(key);
        }
        for(Key<String, Vector2i> key : toBeRemoved)
            guiTextures.remove(key);
        toBeRemoved.clear();
    }

    @Override
    public void delete() {
        for(GuiTexture guiTexture : guiTextures.values())
            guiTexture.delete();
        guiTextures.clear();
        toBeRemoved.clear();
    }

    public void addGuiTexture(String textureName, int width, int height) {
        if(hasGuiTexture(textureName, width, height)) {
            guiTextures.get(tempKey).add();
            return;
        }
        GuiTexture texture = new GuiTexture(textureName).init();
        texture.resize(width, height, Main.guiScale);
        guiTextures.put(new Key<>(textureName, new Vector2i(width, height)), texture);
    }

    public void addGuiTexture(GuiTemplate template, int width, int height) {
        if(hasGuiTexture(template.templateName, width, height)) {
            guiTextures.get(tempKey).add();
            return;
        }
        GuiTexture texture = new GuiTexture(template).init();
        texture.resize(width, height, Main.guiScale);
        guiTextures.put(new Key<>(template.templateName, new Vector2i(width, height)), texture);
    }

    public GuiTexture getGuiTexture(String textureName, int width, int height) {
        if(hasGuiTexture(textureName, width, height))
            return guiTextures.get(tempKey);
        else return null;
    }

    public GuiTexture getGuiTexture(GuiTemplate template, int width, int height) {
        if(hasGuiTexture(template.templateName, width, height))
            return guiTextures.get(tempKey);
        else return null;
    }

    public void removeGuiTexture(GuiTemplate template, int width, int height) {
        if(!hasGuiTexture(template.templateName, width, height))
            return;
        guiTextures.get(tempKey).remove();
    }

    public boolean hasGuiTexture(String textureName, int width, int height) {
        setKey(textureName, width, height);
        return guiTextures.containsKey(tempKey);
    }

    private void setKey(String textureName, int width, int height) {
        this.tempKey.setKey1(textureName);
        this.tempKey.getKey2().set(width, height);
    }

}
