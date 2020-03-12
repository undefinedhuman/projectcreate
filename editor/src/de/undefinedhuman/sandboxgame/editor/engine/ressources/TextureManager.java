package de.undefinedhuman.sandboxgame.editor.engine.ressources;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class TextureManager {

    public static TextureManager instance;

    private HashMap<String, Texture> textures;

    public TextureManager() {

        textures = new HashMap<String, Texture>();
        loadTextures();

    }

    public void addTexture(String name) {

        if(!hasTexture(name)) {

            textures.put(name, RessourceManager.loadTexture(name));

        }

    }

    public void addTexture(String... names) {

        for(String s : names) {

            if(!hasTexture(s)) {

                textures.put(s, RessourceManager.loadTexture(s));

            }

        }

    }

    public void removeTexture(String name) {

        if(hasTexture(name)) {

            textures.get(name).dispose();
            textures.remove(name);

        }

    }

    public void removeTexture(String... names) {

        for(String s : names) {

            if(hasTexture(s)) {

                textures.get(s).dispose();
                textures.remove(s);

            }

        }

    }

    public Texture getTexture(String name) {

        if(hasTexture(name)) {

            return textures.get(name);

        }

        return getTexture("Unknown.png");

    }

    public boolean hasTexture(String name) {

        return textures.containsKey(name);

    }

    public void delete() {

        for(Texture texture : textures.values()) {

            texture.dispose();

        }

        textures.clear();

    }

    public HashMap<String, Texture> getTextures() {
        return textures;
    }

    private void loadTextures() {

        addTexture("Unknown.png");
        addTexture("blank.png");

    }

}
