package de.undefinedhuman.projectcreate.engine.resources.texture;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.undefinedhuman.projectcreate.engine.resources.ResourceManager;
import de.undefinedhuman.projectcreate.engine.utils.Manager;

import java.util.HashMap;

public class TextureManager extends Manager {

    public static TextureManager instance;

    private HashMap<String, TextureValue> textures = new HashMap<>();

    public TextureManager() {
        if (instance == null) instance = this;
    }

    @Override
    public void init() {
        addTexture("Unknown.png", "blank.png");
    }

    @Override
    public void delete() {
        for (TextureValue texture : textures.values())
            texture.delete();
        textures.clear();
    }

    public boolean addTexture(String... names) {
        boolean loaded = false;
        for (String name : names) {
            if (!hasTexture(name)) {
                textures.put(name, new TextureValue(ResourceManager.loadTexture(name)));
                loaded |= hasTexture(name);
            } else textures.get(name).add();
        }
        return loaded;
    }

    public boolean hasTexture(String name) {
        return textures.containsKey(name);
    }

    public void removeTexture(String... names) {
        for (String name : names) {
            if (!hasTexture(name)) continue;
            TextureValue texture = textures.get(name);
            texture.remove();
            if(!texture.remove) continue;
            textures.remove(name);
            texture.delete();
        }
    }

    public TextureRegion getTexture(String name) {
        if (hasTexture(name) || addTexture(name)) return textures.get(name).getTextureRegion();
        return hasTexture("Unknown.png") && !name.equals("Unknown.png") ? getTexture("Unknown.png") : null;
    }

    public Pixmap getPixmap(String name) {
        if (hasTexture(name) || addTexture(name))
            return textures.get(name).getPixmap();
        return hasTexture("Unknown.png") && !name.equals("Unknown.png") ? getPixmap("Unknown.png") : null;
    }

}
