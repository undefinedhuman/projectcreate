package de.undefinedhuman.sandboxgame.engine.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.log.Log;

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

    public static FileHandle loadFile(Paths path, String name) {
        return Gdx.files.internal(path.getPath() + name);
    }

    public static boolean existItem(int id) {
        return loadFile(Paths.ITEM_PATH, id + "/settings.item").exists();
    }

}
