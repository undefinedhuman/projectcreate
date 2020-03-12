package de.undefinedhuman.sandboxgame.editor.engine.ressources;

import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class SoundManager {

    public static SoundManager instance;

    private HashMap<String, Sound> sounds;

    public SoundManager() {

        sounds = new HashMap<String, Sound>();
        // loadSounds();

    }

    public void addSound(String name) {

        if(!hasSound(name)) {

            sounds.put(name, RessourceManager.loadSound(name));

        }

    }

    public void addSound(String... names) {

        for(String s : names) {

            if(!hasSound(s)) {

                sounds.put(s, RessourceManager.loadSound(s));

            }

        }

    }

    public void removeSound(String name) {

        if(hasSound(name)) {

            sounds.get(name).dispose();
            sounds.remove(name);

        }

    }

    public void removeSound(String... names) {

        for(String s : names) {

            if(hasSound(s)) {

                sounds.get(s).dispose();
                sounds.remove(s);

            }

        }

    }

    public Sound getSound(String name) {

        if(hasSound(name)) {

            return sounds.get(name);

        }

        return getSound("dirtSound.wav");

    }

    public boolean hasSound(String name) {

        return sounds.containsKey(name);

    }

    public void delete() {

        for(Sound sound : sounds.values()) {

            sound.dispose();

        }

        sounds.clear();

    }

    public HashMap<String, Sound> getSounds() {
        return sounds;
    }

    private void loadSounds() {

        addSound("dirtSound.wav");

    }

}
