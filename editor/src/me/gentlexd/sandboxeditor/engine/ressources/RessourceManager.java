package me.gentlexd.sandboxeditor.engine.ressources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import me.gentlexd.sandboxeditor.engine.file.Paths;
import me.gentlexd.sandboxeditor.engine.log.Log;

public class RessourceManager {

    public static Texture loadTexture(String path) {

        Texture texture = null;

        try {

            texture = new Texture(Gdx.files.internal(path));
            Log.info("Texture loaded successfully: " + path);

        } catch(Exception ex) {

            Log.error("Error while loading texture: " + path);
            Log.error(ex.toString());

        }

        return texture != null ? texture : loadTexture("Unknown.png");

    }

    public static Music loadMusic(String path) {

        Music music = null;

        try {

            music = Gdx.audio.newMusic(Gdx.files.internal(path));
            Log.info("Music loaded successfully: " + path);

        } catch(Exception ex) {

            Log.error("Error while loading music: " + path);
            Log.error(ex.toString());

        }

        return music;

    }

    public static BitmapFont loadBitmapFont(String path) {

        BitmapFont bitmapFont = null;

        try {

            bitmapFont = new BitmapFont(Gdx.files.internal(path), false);
            Log.info("BitmapFont loaded successfully: " + path);

        } catch(Exception ex) {

            Log.error("Error while loading BitmapFont: " + path);
            Log.error(ex.toString());

        }

        return bitmapFont;

    }

    public static Sound loadSound(String name) {

        Sound sound = null;

        try {

            sound = Gdx.audio.newSound(Gdx.files.internal(Paths.SOUND_FOLDER.getPath() + name));
            Log.info("Sound loaded successfully: " + name);

        } catch(Exception ex) {

            Log.error("Error while loading sound: " + name);
            Log.error(ex.toString());

        }

        return sound != null ? sound : loadSound("dirtSound.wav");

    }

    public static ShaderProgram loadShader(String vertexName, String fragmentName) {

        String vertexShader = Gdx.files.internal("shader/" + vertexName + ".glsl").readString();
        String fragmentShader = Gdx.files.internal("shader/" + fragmentName + ".glsl").readString();
        ShaderProgram shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
        if (!shaderProgram.isCompiled()) Log.error("Error while compiling shader: " + "\n" + shaderProgram.getLog());
        if(shaderProgram.isCompiled()) Log.info("Error while loading music: " + vertexName + ", " + fragmentName);

        return shaderProgram;

    }

    public static FileHandle loadDir(Paths path) {

        return Gdx.files.internal(path.getPath());

    }

    public static FileHandle loadFile(Paths path, String name) {

        return Gdx.files.internal(path.getPath() + name);

    }

}
