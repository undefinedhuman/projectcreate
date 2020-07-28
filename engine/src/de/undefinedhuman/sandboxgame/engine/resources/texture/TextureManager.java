package de.undefinedhuman.sandboxgame.engine.resources.texture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.resources.ResourceManager;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;

import java.util.Arrays;
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

    public boolean addTexture(String... names) {
        boolean loaded = false;
        for (String name : names) {
            if (!hasTexture(name)) {
                textures.put(name, new TextureValue(ResourceManager.loadTexture(name)));
                loaded |= hasTexture(name);
            } else textures.get(name).add();
        }
        if (loaded)
            Log.info("Texture" + Tools.appendSToString(names.length) + " loaded successfully: " + Arrays.toString(names));
        return loaded;
    }

    public boolean hasTexture(String name) {
        return textures.containsKey(name);
    }

    @Override
    public void delete() {
        for (TextureValue texture : textures.values()) texture.delete();
        textures.clear();
    }

    public void removeTexture(String... names) {
        for (String name : names) {
            if (hasTexture(name)) textures.get(name).remove();
            if (textures.containsKey(name) && textures.get(name).remove) textures.remove(name);
        }
    }

    public TextureRegion getTexture(String name) {
        if (hasTexture(name) || addTexture(name)) return textures.get(name).getTexture();
        return hasTexture("Unknown.png") && !name.equals("Unknown.png") ? getTexture("Unknown.png") : null;
    }

}
