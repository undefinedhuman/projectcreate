package de.undefinedhuman.sandboxgame.engine.ressources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.items.ItemType;
import de.undefinedhuman.sandboxgame.engine.items.type.blocks.Block;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.entity.EntityType;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.entity.ecs.blueprint.Blueprint;

import java.util.HashMap;

public class ResourceManager {

    public static Texture loadTexture(String path) {
        Texture texture = null;
        try { texture = new Texture(Gdx.files.internal(path));
        } catch (Exception ex) { Log.error("Error while loading texture: " + path + "\n" + ex.getMessage()); }
        return texture != null ? texture : loadTexture("Unknown.png");
    }

    public static Music loadMusic(String path) {
        Music music = null;
        try {
            music = Gdx.audio.newMusic(Gdx.files.internal(path));
            Log.info("Music loaded successfully: " + path);
        } catch (Exception ex) {
            Log.error("Error while loading music: " + path + "\n" + ex.getMessage());
        }
        return music;
    }


    public static BitmapFont loadFont(String name) {
        return loadBitmapFont("gui/font/" + name + ".fnt");
    }

    public static BitmapFont loadBitmapFont(String path) {
        BitmapFont bitmapFont = null;
        try {
            bitmapFont = new BitmapFont(Gdx.files.internal(path), false);
        } catch (Exception ex) {
            Log.error("Error while loading BitmapFont: " + path + "\n" + ex.getMessage());
        }
        return bitmapFont;
    }

    public static Sound loadSound(String name) {

        Sound sound = null;

        try {
            sound = Gdx.audio.newSound(Gdx.files.internal(name));
            Log.info("Sound loaded successfully: " + name);
        } catch (Exception ex) {
            Log.error("Error while loading sound: " + name + "\n" + ex.getMessage());
        }

        return sound != null ? sound : loadSound("sounds/dirtSound.wav");

    }

    public static ShaderProgram loadShader(String vertexName, String fragmentName) {
        ShaderProgram shaderProgram = new ShaderProgram(Gdx.files.internal("shader/" + vertexName + ".glsl").readString(), Gdx.files.internal("shader/" + fragmentName + ".glsl").readString());
        if (!shaderProgram.isCompiled()) Log.error("Error while compiling shader: " + "\n" + shaderProgram.getLog());
        if (shaderProgram.isCompiled()) Log.info("Error while loading music: " + vertexName + ", " + fragmentName);
        return shaderProgram;
    }

    public static Blueprint loadBlueprint(int id) {

        FileHandle file = loadFile(Paths.ENTITY_FOLDER, id + "/settings.txt");
        FileReader reader = new FileReader(file, true);
        reader.nextLine();
        EntityType type = EntityType.valueOf(reader.getNextString());
        reader.getNextString();
        Vector2 size = reader.getNextVector2();
        int componentSize = reader.getNextInt();
        Blueprint blueprint = new Blueprint(id, type, size);
        for (int i = 0; i < componentSize; i++) {
            reader.nextLine();
            blueprint.addComponentBlueprint(ComponentType.load(reader.getNextString(), reader, id));
        }
        reader.close();
        return blueprint;

    }

    public static FileHandle loadFile(Paths path, String name) {
        return Gdx.files.internal(path.getPath() + name);
    }

    public static Item loadItem(int id) {
        FileHandle file = loadFile(Paths.ITEM_PATH, id + "/settings.txt");
        FileReader reader = new FileReader(file, true);
        reader.nextLine();
        ItemType type = ItemType.valueOf(reader.getNextString());
        reader.nextLine();
        int size = reader.getNextInt();
        HashMap<String, LineSplitter> settings = new HashMap<>();
        for (int i = 0; i < size; i++) {
            reader.nextLine();
            settings.put(reader.getNextString(), new LineSplitter(reader.nextLine(), true, ";"));
        }
        Item item = type.load(type, id, settings);
        reader.close();
        settings.clear();
        if(item == null) return null;
        TextureManager.instance.addTexture(item.getTextures());
        if(item.type == ItemType.BLOCK) ((Block) item).setAtlas(new TextureAtlas(((Block) item).getAtlasPath()));
        item.init();
        return item;
    }

    public static boolean existItem(int id) {
        return loadFile(Paths.ITEM_PATH, id + "/settings.txt").exists();
    }

}
