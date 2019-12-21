package de.undefinedhuman.sandboxgame.engine.ressources.texture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.undefinedhuman.sandboxgame.engine.ressources.ResourceManager;
import de.undefinedhuman.sandboxgame.utils.Manager;

import java.util.HashMap;

public class TextureManager extends Manager {

    // TODO Wenn auf eine Instant einer Texture verwiesen wird also z.b. für irgendeine Klasse nur Texture = TextureManager.getTexture(name) gemacht wird bekommt diese Texture keinen Index wie bei add. (Falls sie schon existiert)

    public static TextureManager instance;

    private HashMap<String, TextureValue> textures;

    public TextureManager() {
        textures = new HashMap<>();
        if(instance == null) instance = this;
    }

    @Override
    public void init() {
        addTexture("Unknown.png");
        addTexture("blank.png");
    }

    public boolean addTexture(String name) {
        if (!hasTexture(name)) textures.put(name, new TextureValue(ResourceManager.loadTexture(name)));
        else textures.get(name).add();
        return hasTexture(name);
    }
    public void addTexture(String... names) { for (String s : names) addTexture(s); }

    public void removeTexture(String name) {
        if(hasTexture(name)) textures.get(name).remove();
        if(textures.get(name).remove) textures.remove(name);
    }
    public void removeTexture(String... names) { for (String s : names) removeTexture(s); }

    public TextureRegion getTexture(String name) {
        TextureValue value = textures.get(name);
        if (value != null || addTexture(name)) return textures.get(name).get();
        return hasTexture("Unknown.png") ? getTexture("Unknown.png") : null;
    }

    public boolean hasTexture(String name) {
        return textures.containsKey(name);
    }

    @Override
    public void delete() {
        for (TextureValue texture : textures.values()) texture.delete();
        textures.clear();
    }

}
