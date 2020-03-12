package de.undefinedhuman.sandboxgame.engine.ressources;

import com.badlogic.gdx.audio.Sound;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;

import java.util.HashMap;

public class SoundManager extends Manager {

    public static SoundManager instance;

    private HashMap<String, Sound> sounds;

    public SoundManager() {
        sounds = new HashMap<>();
        if (instance == null) instance = this;
    }

    @Override
    public void init() {
        addSound("sounds/dirtSound.wav");
    }

    public void addSound(String... names) {
        for (String s : names) if (!hasSound(s)) sounds.put(s, ResourceManager.loadSound(s));
    }

    public boolean hasSound(String name) {
        return sounds.containsKey(name);
    }

    @Override
    public void delete() {
        for (Sound sound : sounds.values()) sound.dispose();
        sounds.clear();
    }

    public void removeSound(String... names) {
        for (String name : names)
            if (hasSound(name)) {
                sounds.get(name).dispose();
                sounds.remove(name);
            }
    }

    public Sound getSound(String name) {
        if (hasSound(name)) return sounds.get(name);
        return getSound("dirtSound.wav");
    }

}
