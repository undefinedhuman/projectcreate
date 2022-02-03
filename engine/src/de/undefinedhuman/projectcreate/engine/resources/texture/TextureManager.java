package de.undefinedhuman.projectcreate.engine.resources.texture;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextureManager implements Manager {

    private static final int MAX_ELEMENTS_DEBUG = 25;

    private static volatile TextureManager instance;

    private HashMap<String, TextureValue> textures = new HashMap<>();

    private TextureManager() { }

    @Override
    public void init() {
        Log.debug(() -> {
            Object[] loadedTextures = addTextures("Unknown.png", "blank.png").toArray();
            return "Texture" + Utils.appendSToString(loadedTextures.length) + " loaded: " + Utils.convertArrayToPrintableString(loadedTextures);
        });
    }

    @Override
    public void delete() {
        deleteTextures(textures.entrySet().stream());
        textures.clear();
    }

    public boolean loadTextures(String... names) {
        Object[] loadedTextures = addTextures(names).toArray();
        Log.debug(() -> {
            if(loadedTextures.length == 0)
                return "";
            return "Texture" + Utils.appendSToString(loadedTextures.length) + " loaded: " + Utils.convertArrayToPrintableString(loadedTextures);
        });
        return Arrays.stream(names).filter(name -> hasTexture(name) && textures.get(name) != null).count() == names.length;
    }

    public Stream<String> addTextures(String... names) {
        Arrays.stream(names)
                .filter(this::hasTexture)
                .map(name -> textures.get(name))
                .forEach(TextureValue::add);
        return Arrays.stream(names)
                .filter(name -> !hasTexture(name))
                .peek(name -> textures.put(name, new TextureValue(RessourceUtils.loadTexture(name))));
    }

    public boolean hasTexture(String name) {
        return textures.containsKey(name);
    }

    public void removeTextures(String... names) {
        Arrays.stream(names)
                .filter(this::hasTexture)
                .map(name -> textures.get(name))
                .forEach(TextureValue::remove);
        Stream<Map.Entry<String, TextureValue>> texturesToBeRemoved = textures
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().remove);
        deleteTextures(texturesToBeRemoved);
    }

    private void deleteTextures(Stream<Map.Entry<String, TextureValue>> texturesToBeRemoved) {
        List<Map.Entry<String, TextureValue>> entries = texturesToBeRemoved.collect(Collectors.toList());
        if(entries.size() == 0)
            return;
        List<String> deletedTextures = entries.stream()
                .map(entry -> {
                    textures.remove(entry.getKey());
                    entry.getValue().delete();
                    return entry.getKey();
                }).collect(Collectors.toList());
        Log.debug(() -> "Texture" + Utils.appendSToString(entries.size()) + " unloaded: "
                + deletedTextures.stream().limit(MAX_ELEMENTS_DEBUG).collect(Collectors.joining(", "))
                + (deletedTextures.size() > MAX_ELEMENTS_DEBUG ? " +" + (deletedTextures.size() - MAX_ELEMENTS_DEBUG) + " more" : ""));
    }

    public TextureRegion getTexture(String name) {
        if (hasTexture(name) || addTextures(name).count() == 1)
            return textures.get(name).getTextureRegion();
        return hasTexture("Unknown.png") && !name.equals("Unknown.png") ? getTexture("Unknown.png") : null;
    }

    public Pixmap getPixmap(String name) {
        if (hasTexture(name) || loadTextures(name))
            return textures.get(name).getPixmap();
        return hasTexture("Unknown.png") && !name.equals("Unknown.png") ? getPixmap("Unknown.png") : null;
    }

    public static TextureManager getInstance() {
        if (instance == null) {
            synchronized (TextureManager.class) {
                if (instance == null)
                    instance = new TextureManager();
            }
        }
        return instance;
    }

}
